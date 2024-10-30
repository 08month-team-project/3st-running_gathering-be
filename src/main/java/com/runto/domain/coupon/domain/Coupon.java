package com.runto.domain.coupon.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private Long quantity;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "coupon")
    private Set<UserCoupons> userCoupons = new HashSet<>();

    public void decreaseQuantity(Long quantity) {

        if (this.quantity - quantity < 0) {
            throw new RuntimeException("쿠폰 수량이 없습니다.");
        }
        this.quantity -= quantity;
    }

    public static Coupon createCoupon(String couponName, Long quantity, LocalDateTime expiredAt) {
        return Coupon.builder()
                .couponName(couponName)
                .quantity(quantity)
                .expiredAt(expiredAt)
                .build();
    }

}
