package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.domain.RegionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegionCodeDto {

    private Integer codeH; // 행정동 코드

    private Integer codeB; // 법정동 코드

    public static RegionCodeDto from(RegionCode regionCode) {

        return RegionCodeDto.builder()
                .codeH(regionCode.getCodeH())
                .codeB(regionCode.getCodeB())
                .build();
    }

    public RegionCode toEntity() {
        return RegionCode.builder()
                .codeH(codeH)
                .codeB(codeB)
                .build();
    }
}
