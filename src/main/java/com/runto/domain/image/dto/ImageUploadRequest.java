package com.runto.domain.image.dto;

import com.runto.domain.image.exception.ImageException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.runto.global.exception.ErrorCode.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageUploadRequest {

    List<ImageDto> images;
    private int representativeImageIndex;

    public static ImageUploadRequest of(int representativeImageIndex,
                                        List<MultipartFile> imageFiles,
                                        int[] contentImageOrders) {

        if (imageFiles == null || imageFiles.size() < 1) {
            throw new ImageException(INVALID_FILE);
        }
        if (representativeImageIndex < 0) representativeImageIndex = 0;

        if (contentImageOrders == null || contentImageOrders.length != imageFiles.size()) {
            throw new ImageException(IMAGE_ORDER_MISMATCH);
        }
        if (representativeImageIndex > imageFiles.size() - 1) {
            throw new ImageException(INVALID_REPRESENTATIVE_IMAGE_INDEX);
        }

        List<ImageDto> images = new ArrayList<>();
        IntStream.range(0, imageFiles.size())
                .forEach(i -> images.add(new ImageDto(imageFiles.get(i), contentImageOrders[i])));

        return ImageUploadRequest.builder()
                .representativeImageIndex(representativeImageIndex)
                .images(images)
                .build();
    }
}
