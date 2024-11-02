package com.runto.domain.image.application;

import com.runto.domain.image.dao.GatheringImageRepository;
import com.runto.domain.image.dto.*;
import com.runto.domain.image.type.ImageUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3ImageService s3ImageService;
    private final GatheringImageRepository gatheringImageRepository;

    public ImageRegisterResponse registerGatheringImages(ImageUploadRequest uploadRequest) {

        if (uploadRequest == null) return null;

        List<ImageUrlDto> contentImageUrls = s3ImageService
                .uploadContentImages(uploadRequest.getImages(), "gathering");

        return ImageRegisterResponse.of(
                uploadRequest.getRepresentativeImageIndex(), contentImageUrls);
    }

    public void moveImageFromTempToPermanent(List<ImageUrlDto> contentImageUrls) {

        if (contentImageUrls == null || contentImageUrls.isEmpty()) return;

        contentImageUrls.forEach(imageUrlDto ->
                s3ImageService.moveImageProcess(imageUrlDto.getImageUrl()));
    }

    public ImageUrlsResponse getGatheringImages(Long gatheringId) {

        return new ImageUrlsResponse(
                gatheringImageRepository.findGatheringImagesByGatheringId(gatheringId).stream()
                        .map(ImageUrlDto::from)
                        .toList());
    }


    public ImageUrlDto updateUserProfile(Long userId, MultipartFile newImageFile, String oldImageUrl) {

        s3ImageService.deleteS3Object(oldImageUrl, "temp/"); // TODO 나중에 이미지 버그 수정 및 리팩토링 때 수정될 수도 있음

        if (newImageFile == null || newImageFile.isEmpty()) {
            return new ImageUrlDto(null, 0);
        }

        return s3ImageService.uploadContentImages(
                List.of(new ImageDto(newImageFile, 0)),
                        String.format("profile[%s]", userId)).get(0);
    }
}
