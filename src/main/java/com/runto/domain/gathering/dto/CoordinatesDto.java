package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Coordinates;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CoordinatesDto {

    @NotNull(message = "x값은 필수입니다.")
    private Double x;

    @NotNull(message = "y값은 필수입니다.")
    private Double y;

    public static CoordinatesDto from(Coordinates coordinates) {
        return CoordinatesDto.builder()
                .x(coordinates.getX())
                .y(coordinates.getY())
                .build();
    }

    public Coordinates toEntity() {
        return new Coordinates(x, y);
    }
}
