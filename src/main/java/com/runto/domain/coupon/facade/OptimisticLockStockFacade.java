package com.runto.domain.coupon.facade;

import com.runto.domain.coupon.application.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final CouponService couponService;

    public void requestCouponV2(Long couponId) throws InterruptedException {
        while (true) {
            try {
                couponService.requestCouponV2(couponId);
                break;
            }catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
