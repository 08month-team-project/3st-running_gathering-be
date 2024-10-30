package com.runto.domain.admin.application;

import com.runto.domain.admin.dao.CouponRepository;
import com.runto.domain.admin.domain.Coupon;
import com.runto.domain.admin.dto.CouponRequest;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public List<MonthUserResponse> getUserByMonth(UserStatus status) {
        return userRepository.findAllByUserByMonth(status);
    }

    public UserCountResponse getUserCount(AdminStatsCount statsCount) {
        return userRepository.countUsersByType(statsCount);
    }

    @Transactional
    public void addCoupon(CouponRequest request) {

        Coupon coupon = Coupon.createCoupon(request.getCouponName(), request.getQuantity(), request.getExpiredAt());
        couponRepository.save(coupon);

    }
}
