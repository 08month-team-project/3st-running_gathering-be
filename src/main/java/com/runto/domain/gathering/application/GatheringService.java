package com.runto.domain.gathering.application;


import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.CreateGatheringRequest;
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

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GatheringService {

    private final ImageService imageService;

    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;


    // TODO: 회원관련 기능 dev에 머지되면 param 에 UserDetails 추가 & 교체 , user 관련 예외로 수정
    @Transactional
    public void createGatheringGeneral(CreateGatheringRequest request) {

        User findUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("없는 유저"));

        Gathering gathering = request.toEntity(findUser);
        addContentImages(request.getGatheringImageUrls(), gathering);

        gatheringRepository.save(gathering);

        // s3 temp 경로에 있던 이미지파일들을 정식 경로에 옮기기
        imageService.moveImageFromTempToPermanent(request.getGatheringImageUrls()
                .getContentImageUrls());
    }

    private void addContentImages(GatheringImageUrlsDto imageUrlDto, Gathering gathering) {
        if(imageUrlDto == null) return;

        List<GatheringImage> gatheringImages = imageUrlDto.getContentImageUrls().stream()
                .map(ImageUrlDto::toEntity)
                .toList();
        gathering.addContentImages(gatheringImages);
    }
}
