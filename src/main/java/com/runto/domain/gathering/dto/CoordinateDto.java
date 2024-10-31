package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.domain.Coordinates;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoordinateDto {

    @NotNull(message = "x 좌표값은 필수값입니다.")
    private Double x;

    @NotNull(message = "y 좌표값은 필수값입니다.")
    private Double y;


    public Coordinates toEntity() {
        return new Coordinates(x, y);
    }

}
