package com.runto.domain.gathering.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Coordinates {
    @Column(precision = 9, scale = 4, nullable = false)
    private BigDecimal x;

    @Column(precision = 9, scale = 4, nullable = false)
    private BigDecimal y;
}
