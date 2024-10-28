package com.runto.domain.image.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.image.exception.ImageException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.runto.global.exception.ErrorCode.INVALID_REPRESENTATIVE_IMAGE_INDEX;

@Builder
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageRegisterResponse {

    private Integer representativeImageIndex;

    @Valid
    @Size(max = 3, message = "이미지 등록은 최대 3개까지만 허용됩니다.")
    private List<ImageUrlDto> contentImageUrls;

    public static ImageRegisterResponse of(Integer representativeImageIndex, List<ImageUrlDto> imageUrls) {

        if (representativeImageIndex == null || representativeImageIndex < 0) {
            representativeImageIndex = 0;
        }
        if (imageUrls == null || representativeImageIndex > imageUrls.size()) {
            throw new ImageException(INVALID_REPRESENTATIVE_IMAGE_INDEX);
        }

        return ImageRegisterResponse.builder()
                .representativeImageIndex(representativeImageIndex)
                .contentImageUrls(imageUrls)
                .build();
    }


    public String getRepresentativeImageUrl() {
        return contentImageUrls
                .get(representativeImageIndex)
                .getImageUrl();
    }

}
