package com.runto.domain.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "coupon_name")
    private String couponName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;

    public static Coupon createCoupon(String couponName, Integer quantity, LocalDateTime expiredAt) {
        return Coupon.builder()
                .couponName(couponName)
                .quantity(quantity)
                .expiredAt(expiredAt)
                .build();
    }

}
