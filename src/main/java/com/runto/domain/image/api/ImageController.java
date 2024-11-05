package com.runto.domain.image.api;

import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.image.dto.ImageUploadRequest;
import com.runto.domain.image.type.ImageUrlsResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "모임글 이미지 등록 (1~3개 가능)")
    @PostMapping("/gatherings")
    public ResponseEntity<ImageRegisterResponse> registerGatheringImages(
            @RequestPart(value = "representative_image_index", required = false) Integer representativeImageIndex,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "image_order", required = false) int[] imageOrder) { // 안에 하나하나 null 체크 하는 것보다 0으로 받기로 함

        log.info("모임글 이미지 등록 컨트롤러 진입={}", images.get(0));
        return ResponseEntity.ok(imageService.registerGatheringImages(
                ImageUploadRequest.of(representativeImageIndex, images, imageOrder)));
    }

    @Operation(summary = "모임글 이미지주소 목록 가져오기 (모임글 상세조회에 사용)")
    @GetMapping
    public ResponseEntity<ImageUrlsResponse> getGatheringImages(
            @RequestParam("gathering_id") Long gatheringId) {

        return ResponseEntity.ok(imageService.getGatheringImages(gatheringId));
    }
}
