package com.runto.domain.image.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.image.domain.GatheringImage;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ImageUrlDto {

    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*\\.(webp)$", message = "유효하지 않은 이미지 URL입니다.")
    private String imageUrl;

    private int order;

    public static ImageUrlDto from(GatheringImage image) {
        return ImageUrlDto.builder()
                .imageUrl(image.getImageUrl())
                .order(image.getImageOrder())
                .build();
    }

    public GatheringImage toGathering() { // Gathering 외에도 쓰게돼서 엔티티이름으로 바꿈
        return GatheringImage.builder()
                .imageUrl(imageUrl)
                .imageOrder(order)
                .build();
    }
}
