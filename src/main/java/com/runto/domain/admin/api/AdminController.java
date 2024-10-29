package com.runto.domain.admin.api;

import com.runto.domain.admin.application.AdminService;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/user/monthly-status")
    public ResponseEntity<List<MonthUserResponse>> getUserByMonth(@RequestParam UserStatus status) {
        List<MonthUserResponse> responses = adminService.getUserByMonth(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("user/statsCount")
    public ResponseEntity<UserCountResponse> getUserCount(@RequestParam AdminStatsCount statsCount) {
        UserCountResponse response = adminService.getUserCount(statsCount);
        return ResponseEntity.ok(response);
    }
}
