package com.runto.domain.gathering.application;


import com.runto.domain.gathering.dao.EventGatheringRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.EventGathering;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.CreateGatheringRequest;
import com.runto.domain.gathering.dto.GatheringDetailResponse;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.dto.UserGatheringsResponse;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.GatheringType;
import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.domain.GatheringImage;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringType.EVENT;
import static com.runto.domain.gathering.type.GatheringType.GENERAL;
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


    // TODO: 만약 신고기능 구현하는거면 나중에 관련 로직 추가 필요
    // TODO: 날짜 설정 검증 로직 필요 (설정 날짜 관련 서비스 정책? 정하고 추후에 추가)
    // TODO: 그룹 채팅방 생성 로직 추가
    // TODO moveImageProcess 에러 해결되면 주석 풀기
    @Transactional
    public void createGatheringGeneral(Long userId, CreateGatheringRequest request) {

        validateMaxNumber(request.getGatheringType(), request.getMaxNumber());
        createGathering(userId, request);

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
//        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
//                .getContentImageUrls());
    }

    // TODO: 설정 날짜 관련 서비스 정책? 정하고 구현
    private void validateDate(LocalDateTime deadLine, LocalDateTime appointedAt) {

        // 마감날짜 - 적어도 현재기준 X시간 이후
        // 약속날짜 - 적어도 현재기준 X시간 이후
        // 마감날짜 vs 약속날짜 는 적어도 X 시간차이가 나야함
        return;
    }

    private void addContentImages(ImageRegisterResponse imageUrlDto, Gathering gathering) {
        if (imageUrlDto == null) return;

        List<GatheringImage> gatheringImages = imageUrlDto.getContentImageUrls().stream()
                .map(ImageUrlDto::toEntity)
                .toList();
        gathering.addContentImages(gatheringImages);
    }

    public GatheringDetailResponse getGatheringDetail(Long gatheringId) {

        Gathering gathering = gatheringRepository.findGatheringDetailById(gatheringId)
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        return GatheringDetailResponse.from(gathering);
    }


    // 내 모임목록조회에만 사용되지 않고 특정유저의 모임목록을 조회할 수 있는 상황을 고려하였음
    public UserGatheringsResponse getUserGatherings(Long userId,
                                                    Pageable pageable,
                                                    UserGatheringsRequestParams requestParams) {

        return UserGatheringsResponse.fromGatherings(
                gatheringRepository.getUserGatherings(userId, pageable, requestParams));
    }


    // TODO: 회원관련 기능 dev에 머지되면 param 에 UserDetails 추가 & 교체
    // TODO moveImageProcess 에러 해결되면 주석 풀기
    // db를 세번 오가는 상황, 그렇다고 gathering에서 양방향으로 넣기엔 안 어울리는 듯 하다.
    @Transactional
    public void requestEventGatheringHosting(Long userId,
                                             CreateGatheringRequest request) {

        validateMaxNumber(request.getGatheringType(), request.getMaxNumber());

        Gathering gathering = createGathering(userId, request);
        eventGatheringRepository.save(new EventGathering(gathering));

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
//        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
//                .getContentImageUrls());

    }

    private Gathering createGathering(Long userId, CreateGatheringRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        validateDate(request.getDeadline(), request.getAppointedAt());

        Gathering gathering = request.toEntity(user);
        gathering.addMember(user, ORGANIZER);
        addContentImages(request.getImageRegisterResponse(), gathering);

        gatheringRepository.save(gathering);
        return gathering;
    }

    // 커스텀 애노테이션 에러 해결되면 삭제
    private void validateMaxNumber(GatheringType type, int maxNumber) {
        if (GENERAL.equals(type) && (maxNumber < 2 || maxNumber > 10)) {
            throw new GatheringException(GENERAL_MAX_NUMBER);
        }
        if (EVENT.equals(type) && (maxNumber < 10 || maxNumber > 300)) {
            throw new GatheringException(EVENT_MAX_NUMBER);
        }
    }

}
