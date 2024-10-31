package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Location;
import jakarta.validation.Valid;
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

    @Valid
    @NotNull(message = "주소는 필수값입니다.")
    private AddressNameDto addressNames;

    @Valid
    @NotNull(message = "위치 좌표는 필수값입니다.")
    private CoordinatesDto coordinates;

    // 프론트 상황 때문에 필수값으로 지정하진 않음
    private RegionCodeDto regionCode;

    public static LocationDto from(Location location) {
        return LocationDto.builder()
                .addressNames(AddressNameDto.from(location.getAddressName()))
                .coordinates(CoordinatesDto.from(location.getCoordinates()))
                .regionCode(RegionCodeDto.from(location.getRegionCode()))
                .build();
    }

    public Location toLocation() {
        return Location.builder()
                .addressName(addressNames.toEntity())
                .coordinates(coordinates.toEntity())
                .regionCode(regionCode.toEntity())
                .build();
    }
}
