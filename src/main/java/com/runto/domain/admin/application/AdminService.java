package com.runto.domain.admin.application;

import com.runto.domain.admin.dto.*;
import com.runto.domain.admin.type.AdminEventCount;
import com.runto.domain.admin.type.AdminEventCount;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import com.runto.domain.coupon.dao.CouponRepository;
import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.dto.CouponRequest;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.email.application.EmailService;
import com.runto.domain.gathering.dao.EventGatheringRepository;
import com.runto.domain.gathering.dao.GatheringRepository;
import com.runto.domain.gathering.domain.EventGathering;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.EventRequestStatus;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.domain.user.type.UserStatus;
import com.runto.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.runto.domain.gathering.type.EventRequestStatus.APPROVED;
import static com.runto.domain.gathering.type.EventRequestStatus.REJECTED;
import static com.runto.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final GatheringRepository gatheringRepository;
    private final EventGatheringRepository eventGatheringRepository;
    private final EmailService emailService;

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

    public List<PenaltyDetailsResponse> getPenaltiesByUser(UserStatus status, Pageable pageable) {
        return userRepository.findAllByPenalties(status,pageable).getContent();
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

    @Transactional
    public ApprovalStatusResponse updateEventApprovalStatus(Long eventId, ApprovalStatusRequest request) {
        EventGathering eventGathering = eventGatheringRepository.findById(eventId)
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Gathering gathering = gatheringRepository.findById(eventGathering.getGathering().getId())
                .orElseThrow(() -> new GatheringException(GATHERING_NOT_FOUND));

        // 주최자인지 확인
        if (!gathering.getOrganizerId().equals(user.getId())) {
            throw new UserException(NOT_EVENT_ORGANIZER);
        }

        eventGathering.updateStatus(request.getStatus());

        String recipientEmail = user.getEmail(); // 이벤트 주최자의 이메일

        // 이메일 내용을 설정
        String statusDescription = eventGathering.getStatus().getDescription();
        String reportReason = request.getReportReason(); // 거절 사유

        // 이메일 전송
        emailService.sendApprovalStatusEmail(recipientEmail, gathering.getTitle(), statusDescription, reportReason);

        return ApprovalStatusResponse.builder()
                .message("성공적으로 처리 되었습니다.")
                .build();
    }
}
