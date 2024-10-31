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
public class RegionCode {

    @Column(name = "h_code")
    private Integer codeH; // 행정동 코드

    @Column(name = "b_code")
    private Integer codeB; // 법정동 코드

    public static RegionCode of(Integer codeH, Integer codeB) {

        return RegionCode.builder()
                .codeH(codeH)
                .codeB(codeB)
                .build();
    }
}
