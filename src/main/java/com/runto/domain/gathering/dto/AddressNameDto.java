package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AddressNameDto { // 변환이 의도대로 되지 않아서 직접 JsonProperty 로 적용

    @JsonProperty("address_name")
    @NotBlank(message = "주소이름은 필수값입니다.")
    private String addressName;

    @NotBlank(message = "region_1depth_name 은 필수값입니다.")
    @JsonProperty("region_1depth_name")
    private String region1depthName;

    @NotBlank(message = "region_2depth_name 은 필수값입니다.")
    @JsonProperty("region_2depth_name")
    private String region2depthName;

    @NotBlank(message = "region_3depth_name 은 필수값입니다.")
    @JsonProperty("region_3depth_name")
    private String region3depthName;


    public static AddressNameDto from(AddressName name) {

        return AddressNameDto.builder()
                .addressName(name.getAddressName())
                .region1depthName(name.getRegion1DepthName())
                .region2depthName(name.getRegion2DepthName())
                .region3depthName(name.getRegion3DepthName())
                .build();
    }

    public AddressName toEntity() {
        return AddressName.builder()
                .addressName(addressName)
                .region1DepthName(region1depthName)
                .region2DepthName(region2depthName)
                .region3DepthName(region3depthName)
                .build();
    }
}
