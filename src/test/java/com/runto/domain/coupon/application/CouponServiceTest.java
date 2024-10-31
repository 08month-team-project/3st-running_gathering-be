package com.runto.domain.coupon.application;

import com.runto.domain.coupon.dao.CouponRepository;
import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.facade.OptimisticLockStockFacade;
import com.runto.global.config.QueryDSLConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(QueryDSLConfig.class)
@SpringBootTest
@ActiveProfiles("test")
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    public void before() {

        Coupon coupon = Coupon.createCoupon("선글라스", 100L, LocalDateTime.now().plusDays(30));
        couponRepository.save(coupon);
    }

    @AfterEach
    public void after() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100개의 요청 비관")
    public void 동시에_100개의_요청_비관() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    couponService.requestCouponV1(1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        // when
        Coupon coupon = couponRepository.findById(1L).orElseThrow();
        // then
        assertEquals(0, coupon.getQuantity());
    }

    @Test
    @DisplayName("동시에 100개의 요청 낙관")
    public void 동시에_100개의_요청_낙관() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    try {
                        optimisticLockStockFacade.requestCouponV2(1L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        // when
        Coupon coupon = couponRepository.findById(1L).orElseThrow();
        // then
        assertEquals(0, coupon.getQuantity());
    }

}