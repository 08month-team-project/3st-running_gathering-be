package com.runto.domain.gathering.application;


import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.CreateGatheringRequest;
import com.runto.domain.gathering.dto.GatheringDetailResponse;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.domain.GatheringImage;
import com.runto.domain.image.dto.GatheringImageUrlsDto;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.global.exception.ErrorCode.GATHERING_NOT_FOUND;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GatheringService {

    private final ImageService imageService;

    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;


    // TODO: 회원관련 기능 dev에 머지되면 param 에 UserDetails 추가 & 교체 , user 관련 예외로 수정
    // TODO: 만약 신고기능 구현하는거면 나중에 관련 로직 추가 필요
    // TODO: 날짜 설정 검증 로직 필요 (설정 날짜 관련 서비스 정책? 정하고 추후에 추가)
    @Transactional
    public void createGatheringGeneral(CreateGatheringRequest request) {

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("없는 유저"));

        validateDate(request.getDeadline(), request.getAppointedAt());

        Gathering gathering = request.toEntity(user);
        gathering.addMember(user, ORGANIZER);
        addContentImages(request.getGatheringImageUrls(), gathering);

        gatheringRepository.save(gathering);

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
        // TODO moveImageProcess 에러 해결되면 주석 풀기
//        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
//                .getContentImageUrls());
    }
    
    // TODO: 설정 날짜 관련 서비스 정책? 정하고 구현
    private void validateDate(LocalDateTime deadLine, LocalDateTime appointedAt) {
        return;
    }

    private void addContentImages(GatheringImageUrlsDto imageUrlDto, Gathering gathering) {
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
}
