package com.runto.domain.gathering.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {

    @Embedded
    private AddressName addressName;

    @Embedded
    private Coordinates coordinates;

    @Embedded
    private RegionCode regionCode;

    public static Location of(AddressName addressName,
                              RegionCode regionCode,
                              Coordinates coordinates) {

        return Location.builder()
                .addressName(addressName)
                .regionCode(regionCode)
                .coordinates(coordinates)
                .build();
    }
}
