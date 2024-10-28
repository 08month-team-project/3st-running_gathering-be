package com.runto.domain.image.application;

import com.runto.domain.image.dao.GatheringImageRepository;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.image.dto.ImageUploadRequest;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.image.type.ImageUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3GatheringImageService s3GatheringImageService;
    private final GatheringImageRepository gatheringImageRepository;

    public ImageRegisterResponse registerGatheringImages(ImageUploadRequest uploadRequest) {

        if (uploadRequest == null) return null;

        List<ImageUrlDto> contentImageUrls = s3GatheringImageService
                .uploadContentImages(uploadRequest.getImages());

        return ImageRegisterResponse.of(
                uploadRequest.getRepresentativeImageIndex(), contentImageUrls);
    }

    public void moveImageFromTempToPermanent(List<ImageUrlDto> contentImageUrls) {

        if (contentImageUrls == null || contentImageUrls.isEmpty()) return;

        contentImageUrls.forEach(imageUrlDto ->
                s3GatheringImageService.moveImageProcess(imageUrlDto.getImageUrl()));
    }

    public ImageUrlsResponse getGatheringImages(Long gatheringId) {

        return new ImageUrlsResponse(
                gatheringImageRepository.findGatheringImagesByGatheringId(gatheringId).stream()
                        .map(ImageUrlDto::from)
                        .toList());
    }

}
