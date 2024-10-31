package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.domain.AddressName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressNameDto {

    @NotBlank(message = "주소이름은 필수값입니다.")
    private String addressName;

    @NotBlank(message = "region_1depth_name 은 필수값입니다.")
    private String region1DepthName;

    @NotBlank(message = "region_2depth_name 은 필수값입니다.")
    private String region2DepthName;

    @NotBlank(message = "region_3depth_name 은 필수값입니다.")
    private String region3DepthName;


    public static AddressNameDto from(AddressName name) {

        return AddressNameDto.builder()
                .addressName(name.getAddressName())
                .region1DepthName(name.getRegion1DepthName())
                .region2DepthName(name.getRegion2DepthName())
                .region3DepthName(name.getRegion3DepthName())
                .build();
    }

    public AddressName toEntity() {
        return AddressName.builder()
                .addressName(addressName)
                .region1DepthName(region1DepthName)
                .region2DepthName(region2DepthName)
                .region3DepthName(region3DepthName)
                .build();
    }
}
