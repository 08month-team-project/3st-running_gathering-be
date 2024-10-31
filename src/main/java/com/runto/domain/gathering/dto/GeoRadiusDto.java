package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GeoRadiusDto {

    @Valid
    @NotNull(message = "좌표 값은 필수값입니다.")
    private CoordinatesDto coordinates;

    @NotNull(message = "거리반경은 필수값입니다.")
    @DecimalMin(value = "1.0", inclusive = true, message = "값은 1km 이상이어야 합니다.")
    @DecimalMax(value = "10.0", inclusive = true, message = "값은 10km 이하이어야 합니다.")
    private Double radiusDistance;


    // TODO: ModelAttribute를  적용하는 곳이 많아지면 별도의 필터를 구현할 것을 고려
    // snake case를 처리할 수 있는 별도의 Setter 선언
    public void setCoordinates(CoordinatesDto coordinates) {
        this.coordinates = coordinates;
    }

    public void setRadius_distance(Double radiusDistance) {
        this.radiusDistance = radiusDistance;
    }
}
