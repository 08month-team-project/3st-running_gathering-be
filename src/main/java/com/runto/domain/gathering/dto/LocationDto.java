package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationDto {

    @NotBlank(message = "주소는 필수값입니다.")
    private String addressName;

    @NotNull(message = "위치 좌표는 필수값입니다.")
    private CoordinatesDto coordinates;

    public static LocationDto from(Location location) {
        return LocationDto.builder()
                .addressName(location.getAddressName())
                .coordinates(CoordinatesDto.from(location.getCoordinates()))
                .build();
    }

    public Location toLocation() {
        return Location.builder()
                .addressName(addressName)
                .coordinates(coordinates.toCoordinates())
                .build();
    }
}
