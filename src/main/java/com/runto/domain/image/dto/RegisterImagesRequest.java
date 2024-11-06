package com.runto.domain.image.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterImagesRequest {

    private int representativeImageIndex;

    @NotNull(message = "imageOrders 은 필수값입니다.")
    @Size(min = 1, max = 3, message = "imageOrders 값은 필수값입니다.")
    private int[] imageOrders;
}
