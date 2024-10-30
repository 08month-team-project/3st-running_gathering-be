package com.runto.domain.coupon.application;

import com.runto.domain.coupon.dao.UserCouponsRepository;
import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.dao.CouponRepository;
import com.runto.domain.coupon.domain.UserCoupons;
import com.runto.domain.coupon.type.CouponStatus;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponsRepository userCouponsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void requestCoupon(Long couponId, CustomUserDetails userDetails) {

        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new RuntimeException("해당 쿠폰을 찾을수 없습니다."));

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow( () -> new UserException(ErrorCode.USER_NOT_FOUND));

        coupon.decreaseQuantity(1L);

        UserCoupons userCoupons = UserCoupons.of(user, coupon, LocalDateTime.now(), CouponStatus.AWAITING);

        Optional.of(userCoupons)
                .filter(uc -> userCouponsRepository.existsByUserAndCoupon(user, coupon))
                .ifPresent(uc -> {
                    throw new RuntimeException("쿠폰은 한번만 요청이 가능합니다.");
                });

        userCouponsRepository.save(userCoupons);
    }

}
