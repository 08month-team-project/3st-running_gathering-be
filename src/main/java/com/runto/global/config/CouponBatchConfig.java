package com.runto.global.config;

import com.runto.domain.coupon.dao.UserCouponsRepository;
import com.runto.domain.coupon.domain.UserCoupons;
import com.runto.domain.coupon.type.CouponStatus;
import com.runto.domain.email.application.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CouponBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final UserCouponsRepository userCouponsRepository;
    private final EmailService emailService;

    @Bean
    public Job couponJob() {
        return new JobBuilder("couponJob", jobRepository)
                .start(couponStep())
                .build();
    }

    @Bean
    public Step couponStep() {
        return new StepBuilder("couponStep", jobRepository)
                .<UserCoupons, UserCoupons>chunk(10, platformTransactionManager)
                .reader(userCouponsReader())
                .writer(emailWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<UserCoupons> userCouponsReader() {
        return new RepositoryItemReaderBuilder<UserCoupons>()
                .name("userCouponsReader")
                .pageSize(10)
                .methodName("findByStatus")
                .arguments(Collections.singletonList(CouponStatus.AWAITING))
                .repository(userCouponsRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemWriter<UserCoupons> emailWriter() {
        return items -> {
            for (UserCoupons userCoupon : items) {
                // 이메일 발송
                try {
                    emailService.sendRequestCoupon(userCoupon.getUser().getEmail(),userCoupon.getCoupon().getCouponName());
                    // 이메일 발송 후 상태 업데이트
                    userCoupon.updateStatus(CouponStatus.SENT);
                    log.info("쿠폰 발급 성공: 사용자 이메일: {}, 쿠폰명: {}", userCoupon.getUser().getEmail(), userCoupon.getCoupon().getCouponName());
                } catch (Exception e) {
                    // 이메일 발송 실패 처리 (예: 로깅 또는 알림)
                    userCoupon.updateStatus(CouponStatus.ERROR);
                    log.error("쿠폰 발급 실패: 사용자 이메일: {}, 쿠폰명: {}, 오류: {}", userCoupon.getUser().getEmail(), userCoupon.getCoupon().getCouponName(), e.getMessage());
                }
            }
        };
    }
}
