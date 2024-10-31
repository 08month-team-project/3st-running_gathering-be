package com.runto.domain.coupon.api;

import com.runto.domain.coupon.application.CouponService;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PatchMapping("/coupons/{coupon-id}/request")
    public ResponseEntity<String> requestCoupon(@PathVariable("coupon-id") Long couponId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        couponService.requestCoupon(couponId, userDetails);
        return ResponseEntity.ok("쿠폰 요청이 성공적으로 처리되었습니다.");
    }


}
