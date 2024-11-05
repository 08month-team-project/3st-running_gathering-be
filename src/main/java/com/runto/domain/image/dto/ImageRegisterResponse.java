package com.runto.domain.image.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.image.exception.ImageException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ImageRegisterResponse { // 이미지 업로드 api 반환값으로 만들어지고, 모임글 등록 api 에서 그대로 요청값으로도 쓰임

    @Min(value = 0, message = "인덱스는 음수값일 수 없습니다.")
    private int representativeImageIndex;

    @Valid
    @NotNull(message = "등록하려는 이미지 url이  존재하지 않습니다.")
    @Size(min = 1, max = 3, message = "이미지 등록은 1~3개까지만 허용됩니다.")
    private List<ImageUrlDto> contentImageUrls;


    // 이미지 업로드 api 반환값으로써 쓰일 때
    public static ImageRegisterResponse of(int representativeImageIndex, List<ImageUrlDto> imageUrls) {

        validateImageIndex(representativeImageIndex, imageUrls);

        return ImageRegisterResponse.builder()
                .representativeImageIndex(representativeImageIndex)
                .contentImageUrls(imageUrls)
                .build();
    }

    private static void validateImageIndex(int representativeImageIndex, List<ImageUrlDto> imageUrls) {
        if (representativeImageIndex < 0) representativeImageIndex = 0;

        if (imageUrls == null || representativeImageIndex > imageUrls.size() - 1) {
            throw new ImageException(INVALID_REPRESENTATIVE_IMAGE_INDEX);
        }
    }

    public String getRepresentativeImageUrl() {

        validateImageIndex(this.representativeImageIndex, this.contentImageUrls);

        return contentImageUrls
                .get(representativeImageIndex)
                .getImageUrl();
    }

}
