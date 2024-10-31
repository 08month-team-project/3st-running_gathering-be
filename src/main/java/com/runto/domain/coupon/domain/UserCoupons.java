package com.runto.domain.coupon.domain;

import com.runto.domain.coupon.type.CouponStatus;
import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCoupons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    public static UserCoupons of(User user, Coupon coupon, LocalDateTime receivedAt, CouponStatus status) {
        return builder()
                .user(user)
                .coupon(coupon)
                .receivedAt(receivedAt)
                .status(status)
                .build();
    }

}
