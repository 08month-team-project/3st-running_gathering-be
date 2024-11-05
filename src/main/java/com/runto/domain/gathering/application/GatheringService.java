package com.runto.domain.gathering.application;


import com.runto.domain.gathering.dao.EventGatheringRepository;
import com.runto.domain.gathering.dao.GatheringMemberRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.*;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.EventRequestStatus;
import com.runto.domain.gathering.type.GatheringStatus;
import com.runto.domain.gathering.type.GatheringType;
import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.domain.GatheringImage;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.dto.UserCalenderResponse;
import com.runto.domain.user.excepction.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.runto.domain.gathering.type.AttendanceStatus.*;
import static com.runto.domain.gathering.type.EventRequestStatus.APPROVED;
import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringStatus.*;
import static com.runto.domain.gathering.type.GatheringType.EVENT;
import static com.runto.domain.gathering.type.GatheringType.GENERAL;
import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.global.exception.ErrorCode.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GatheringService {

    private final ImageService imageService;

    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final EventGatheringRepository eventGatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;


    // TODO moveImageProcess 에러 해결되면 주석 풀기
    @Transactional
    public void createGatheringGeneral(Long userId, CreateGatheringRequest request) {

        validateMaxNumber(GENERAL, request.getMaxNumber());
        gatheringRepository.save(createGathering(userId, request, GENERAL));

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
//        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
//                .getContentImageUrls());
    }

    // TODO moveImageProcess 에러 해결되면 주석 풀기
    @Transactional
    public void requestEventGatheringHosting(Long userId,
                                             CreateGatheringRequest request) {

        validateMaxNumber(EVENT, request.getMaxNumber());

        Gathering gathering = createGathering(userId, request, EVENT);
        gathering.applyForEvent(); // 해당 모임을 이벤트모임으로 신청
        gatheringRepository.save(gathering);

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
//        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
//                .getContentImageUrls());

    }

    private Gathering createGathering(Long userId, CreateGatheringRequest request, GatheringType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (!ACTIVE.equals(user.getStatus())) {
            throw new UserException(INVALID_CREATE_GATHERING_INACTIVE_USER);
        }

        validateDate(request.getDeadline(), request.getAppointedAt());

        Gathering gathering = request.toEntity(user, type);
        gathering.addMember(user, ORGANIZER);
        addContentImages(request.getImageRegisterResponse(), gathering);

        return gathering;
    }

    private void validateMaxNumber(GatheringType type, int maxNumber) {
        if (GENERAL.equals(type) && (maxNumber < 2 || maxNumber > 10)) {
            throw new GatheringException(GENERAL_MAX_NUMBER);
        }
        if (EVENT.equals(type) && (maxNumber < 10 || maxNumber > 300)) {
            throw new GatheringException(EVENT_GATHERING_MAX_NUMBER);
        }
    }

    // 나중에 시간 설정은 바뀔 수도 있음
    private void validateDate(LocalDateTime deadLine, LocalDateTime appointedAt) {

        LocalDateTime now = LocalDateTime.now();

        // 마감날짜 - 적어도 현재기준 X시간 이후
        if (!deadLine.isAfter(now.plusHours(3))) {
            throw new GatheringException(INVALID_DEADLINE_TOO_SOON);
        }

        // 약속날짜 - 적어도 현재기준 X시간 이후
        if (!appointedAt.isAfter(now.plusHours(6))) {
            throw new GatheringException(INVALID_APPOINTMENT_TOO_SOON);
        }

        // 마감날짜 vs 약속날짜 는 적어도 X 시간차이가 나야함
        if (!appointedAt.isAfter(deadLine.plusHours(2))) {
            throw new GatheringException(INVALID_DEADLINE_APPOINTMENT_INTERVAL);
        }

    }

    private void addContentImages(ImageRegisterResponse imageUrlDto, Gathering gathering) {
        if (imageUrlDto == null) return;

        List<GatheringImage> gatheringImages = imageUrlDto.getContentImageUrls().stream()
                .map(ImageUrlDto::toGathering)
                .toList();
        gathering.addContentImages(gatheringImages);
    }


    // 왜 dto 로 바로 받는 방식으로 수정했는지는 pr 참고 (관련이슈 #110)
    public GatheringDetailResponse getGatheringDetail(Long userId, Long gatheringId) {


        GatheringDetailResponse response = gatheringRepository
                .getGatheringDetailWithUserParticipation(gatheringId, userId);

        validateGatheringAccessibility(userId, response);
        return response;

    }

    private void validateGatheringAccessibility(Long userId, GatheringDetailResponse response) {

        GatheringDetailContentResponse gathering = response.getContent();

        boolean isOrganizer = Objects.equals(response.getOrganizerId(), userId);
        GatheringStatus status = gathering.getStatus();

        if (DELETED.equals(status)) {
            throw new GatheringException(GATHERING_NOT_FOUND);
        }

        // 신고당한 모임글 상세조회는 작성자만 볼 수 있음
        if (!isOrganizer && REPORTED.equals(status)) {
            throw new GatheringException(GATHERING_REPORTED);
        }

        // 승인되지 않은 이벤트모임은 주최자가 아니면 볼 수 없음
        if (EVENT.equals(gathering.getType()) &&
                !isApprovedEventForNonOrganizer(gathering.getEventRequestStatus(), isOrganizer)) {
            throw new GatheringException(EVENT_GATHERING_NOT_APPROVED_ONLY_ORGANIZER_CAN_VIEW);
        }
    }

    private boolean isApprovedEventForNonOrganizer(EventRequestStatus eventRequestStatus,
                                                   boolean isOrganizer) {

        // dto로 받기로 해서 필요 없어짐
        // Gathering 의 타입이 Event 이지만, 이벤트에 관한 데이터가 없을 수도 있는 상황 대비
        if (eventRequestStatus == null) {
            throw new GatheringException(EVENT_GATHERING_NOT_FOUND);
        }

        // 승인되지 않은 이벤트는 주최자 외에는 조회 불가
        if (!isOrganizer) {
            return APPROVED.equals(eventRequestStatus);
        }
        return true;
    }


    // 내 모임목록조회에만 사용되지 않고 특정유저의 모임목록을 조회할 수 있는 상황을 고려하였음
    public UserGatheringsResponse getUserGatherings(Long userId,
                                                    Pageable pageable,
                                                    UserGatheringsRequestParams requestParams) {

        if (EVENT.equals(requestParams.getGatheringType())) {
            return UserGatheringsResponse.fromEventGatherings(
                    gatheringRepository.getUserEventGatherings(userId, pageable, requestParams));
        }

        return UserGatheringsResponse.fromGeneralGatherings(
                gatheringRepository.getUserGeneralGatherings(userId, pageable, requestParams));
    }


    public UserEventGatheringsResponse getUserEventRequests(Long userId, Pageable pageable) {
        return UserEventGatheringsResponse
                .from(eventGatheringRepository.findEventGatheringsByUserId(userId, pageable));
    }

    public UserCalenderResponse getUserMonthlyGatherings(Long userId, int year, int month) {

        return UserCalenderResponse
                .fromGatheringMembers(gatheringRepository
                        .getUserMonthlyGatherings(userId, year, month));

    }

    public GatheringsResponse getGatherings(GatheringsRequestParams params, Pageable pageable) {

        // 일반 모임인 경우
        if (GENERAL.equals(params.getGatheringType())) {
            return GatheringsResponse.fromGeneralGatherings(gatheringRepository
                    .getGeneralGatherings(pageable, params), params);
        }

        // 이벤트 모임인 경우
        return GatheringsResponse.fromEventGatherings(gatheringRepository
                .getEventGatherings(pageable, params), params);
    }

    public Slice<GatheringMemberResponse> getGatheringMembers(Long gatheringId, Pageable pageable) {

        return gatheringMemberRepository
                .findGatheringMembersWithUserByGatheringId(gatheringId, pageable)
                .map(GatheringMemberResponse::from);
    }


    @Transactional
    public List<MemberAttendanceStatusDto> checkAttendanceGeneralGatheringMembers(
            Long userId, Long gatheringId, List<MemberAttendanceStatusDto> requestList) {

        // 일반모임인지, 이벤트 모임인지 확인하고 구성원목록을 가져와야해서 처음부터 패치조인 X
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        if (!GENERAL.equals(gathering.getGatheringType())) {
            throw new GatheringException(INVALID_REQUEST_GATHERING_TYPE);
        }

        // 해당 출석요청 검증
        validateAttendanceRequirements(userId, gathering);


        List<GatheringMember> members = gatheringMemberRepository
                .findGatheringMembersByGatheringId(gatheringId);

        // GatheringMemberId 를 key값으로 O(1) 접근해서  체크하기위해 Map 생성
        Map<Long, GatheringMember> memberMap = members.stream()
                .collect(Collectors.toMap(GatheringMember::getId, member -> member));

        // 요청값이 해당 gathering 에 속하지 않는 memberId일 경우 무시
        for (MemberAttendanceStatusDto request : requestList) {

            Optional.ofNullable(memberMap.get(request.getMemberId()))
                    .ifPresent(member -> member.checkAttendance(request.getStatus(), request.getRealDistance()));
        }

        // TODO: Batch Update 적용
        // 기본설정은 각 엔티티 마다 업데이트 쿼리를 1개씩 날림
        return gatheringMemberRepository.saveAll(members).stream()
                .map(MemberAttendanceStatusDto::from).toList();
    }

    @Transactional
    public AttendanceEventGatheringResponse checkAttendanceEventGatheringMembers(Long userId,
                                                                                 Long gatheringId,
                                                                                 AttendanceEventGatheringRequest request) {

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        if (!EVENT.equals(gathering.getGatheringType())) {
            throw new GatheringException(INVALID_REQUEST_GATHERING_TYPE);
        }

        // 해당 출석요청 검증
        validateAttendanceRequirements(userId, gathering);


        // 요청한 멤버를 정상출석, 뛴 거리 체크 후 개수 반환
        long updatedAttendingCount = gatheringMemberRepository.updateAttendanceAndDistance(
                gatheringId, ATTENDING, request.getRealDistance(), request.getMemberIdList());


        // 참석하지 않은 멤버들을 NOT_ATTENDING 처리 후 개수 반환
        long notAttendingMemberCount = gatheringMemberRepository.updateAttendanceAndDistance(
                gatheringId, NOT_ATTENDING, 0.0, null);

        return new AttendanceEventGatheringResponse(request.getRealDistance(),
                request.getMemberIdList().size(), updatedAttendingCount, notAttendingMemberCount);

    }

    private void validateAttendanceRequirements(Long userId, Gathering gathering) {

        if (!Objects.equals(userId, gathering.getOrganizerId())) {
            throw new GatheringException(INVALID_ATTENDANCE_CHECK_NOT_ORGANIZER);
        }
        if (!NORMAL.equals(gathering.getStatus())) {
            throw new GatheringException(INVALID_ATTENDANCE_CHECK_NOT_NORMAL_GATHERING);
        }
        if (gathering.getAppointedAt().isAfter(LocalDateTime.now())) {
            throw new GatheringException(INVALID_ATTENDANCE_BEFORE_MEETING);
        }
        if (gathering.getAppointedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new GatheringException(INVALID_ATTENDANCE_AFTER_ONE_WEEK);
        }
    }

    @Transactional
    public void updateCompleteGathering(Long userId, Long gatheringId) {

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        gathering.validateBeforeCompletion(userId);

        Long pendingMemberCount = gatheringMemberRepository.countMembers(gatheringId, PENDING);

        // 출석상태가 pending 인 멤버가 있으면 완료체크 불가
        if (pendingMemberCount != null && pendingMemberCount > 0) {
            throw new GatheringException(INVALID_COMPLETE_UNCHECKED_MEMBERS);
        }

        gathering.updateNormalComplete(userId);
    }

    public GatheringsMapResponse getGeneralGatheringMap(Double radiusDistance,
                                                        BigDecimal x, BigDecimal y) {

        if (radiusDistance < 0.5 || radiusDistance > 10) {
            throw new GatheringException(INVALID_RADIUS_RANGE);
        }

        List<Gathering> generalGatheringMap = gatheringRepository
                .getGeneralGatheringMap(radiusDistance, x, y);

        return GatheringsMapResponse.of(radiusDistance, x, y, generalGatheringMap);
    }

//    @Transactional
//    public void participateGathering(Long userId, Long gatheringId) {
//
//    }
}
