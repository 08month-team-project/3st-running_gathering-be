package com.runto.domain.admin.api;

import com.runto.domain.admin.application.AdminService;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.GatheringCountResponse;
import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.coupon.dto.CouponRequest;
import com.runto.domain.user.type.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원 통계(월별 가입 및 탈퇴 수)")
    @GetMapping("/users/monthly-status")
    public ResponseEntity<List<MonthUserResponse>> getUserByMonth(@RequestParam UserStatus status) {
        List<MonthUserResponse> responses = adminService.getUserByMonth(status);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "총 회원 , 신고, 블랙리스트 사용자 수")
    @GetMapping("/users/statsCount")
    public ResponseEntity<UserCountResponse> getUserCount(@RequestParam AdminStatsCount statsCount) {
        UserCountResponse response = adminService.getUserCount(statsCount);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "신고 및 블랙리스트 회원 목록")
    @GetMapping("/users")
    public ResponseEntity<List<PenaltyDetailsResponse>> getReportedUser(@RequestParam UserStatus status,
                                                                        Pageable pageable) {
        List<PenaltyDetailsResponse> penaltyDetails = adminService.getPenaltiesByUser(status,pageable);
        return ResponseEntity.ok(penaltyDetails);
    }

    @Operation(summary = "블랙리스트 해제")
    @PatchMapping("/user/{user_id}/release")
    public ResponseEntity<Void> releaseUser(@PathVariable("user_id") Long userId) {
        adminService.releaseUser(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모임관리 ")
    @GetMapping("gatherings/")
    public ResponseEntity<List<GatheringCountResponse>> manageGathering(@RequestParam AdminGatherStatsCount statsCount) {
        List<GatheringCountResponse> response = adminService.manageGathering(statsCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/coupons")
    public ResponseEntity<String> addCoupon(@Valid @RequestBody CouponRequest request) {
        adminService.addCoupon(request);
        return ResponseEntity.ok("쿠폰이 성공적으로 등록되었습니다.");
    }

}
