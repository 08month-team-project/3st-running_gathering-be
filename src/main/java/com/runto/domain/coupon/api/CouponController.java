package com.runto.domain.coupon.api;

import com.runto.domain.coupon.application.CouponService;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    // 배치 추가
    @PatchMapping("/coupons/{coupon-id}/request")
    public ResponseEntity<String> requestCoupon(@PathVariable("coupon-id") Long couponId,
                                                @RequestParam("value") String value,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception  {
        couponService.requestCoupon(couponId, userDetails);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("couponJob"), jobParameters);

        return ResponseEntity.ok("쿠폰 요청이 성공적으로 처리되었습니다.");
    }

    // 비관 간편 테스트용
    @PatchMapping("v1/coupons/{coupon-id}/request")
    public ResponseEntity<String> requestCouponV1(@PathVariable("coupon-id") Long couponId) {
        couponService.requestCouponV1(couponId);
        return ResponseEntity.ok("쿠폰 요청이 성공적으로 처리되었습니다.");
    }

    // 낙관 간편 테스트용
    @PatchMapping("v2/coupons/{coupon-id}/request")
    public ResponseEntity<String> requestCouponV2(@PathVariable("coupon-id") Long couponId) {
        couponService.requestCouponV2(couponId);
        return ResponseEntity.ok("쿠폰 요청이 성공적으로 처리되었습니다.");
    }


}
