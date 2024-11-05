package com.runto.domain.admin.application;

import com.runto.domain.admin.dto.*;
import com.runto.domain.admin.type.AdminEventCount;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import com.runto.domain.coupon.dao.CouponRepository;
import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.dto.CouponRequest;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.domain.user.type.UserStatus;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final GatheringRepository gatheringRepository;

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

    public List<PenaltyDetailsResponse> getPenaltiesByUser(UserStatus status) {
        return userRepository.findAllByPenalties(status);
    }

    @Transactional
    public void releaseUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new UserException(ErrorCode.USER_NOT_FOUND));
        user.releaseUser();
    }

    public List<GatheringCountResponse> manageGathering(AdminGatherStatsCount statsCount) {
        return gatheringRepository.manageGathering(statsCount);
    }

    public List<?> getEventsPerMonth(AdminEventCount eventCount) {
        return gatheringRepository.getEventsPerMonth(eventCount);
    }

    public Slice<EventListResponse> getPendingApprovalEventList(Pageable pageable) {
        return gatheringRepository.getPendingApprovalEventList(pageable);
    }
}
