package com.runto.domain.gathering.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AddressName {

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "region_1depth_name")
    private String region1DepthName;

    @Column(name = "region_2depth_name")
    private String region2DepthName;

    @Column(name = "region_3depth_name")
    private String region3DepthName;

    public static AddressName of(String addressName,
                                 String region1DepthName,
                                 String region2DepthName,
                                 String region3DepthName) {

        return AddressName.builder()
                .addressName(addressName)
                .region1DepthName(region1DepthName)
                .region2DepthName(region2DepthName)
                .region3DepthName(region3DepthName)
                .build();

    }

}
