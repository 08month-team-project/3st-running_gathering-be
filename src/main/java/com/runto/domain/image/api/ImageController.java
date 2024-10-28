package com.runto.domain.image.api;

import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.image.dto.ImageUploadRequest;
import com.runto.domain.image.type.ImageUrlsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/gathering")
    public ResponseEntity<ImageRegisterResponse> registerGatheringImages(
            @RequestPart(value = "representative_image_index", required = false) Integer representativeImageIndex,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "image_order", required = false) int[] imageOrder) { // 안에 하나하나 null 체크 하는 것보다 0으로 받기로 함

        return ResponseEntity.ok(imageService.registerGatheringImages(
                ImageUploadRequest.of(representativeImageIndex, images, imageOrder)));
    }
}
