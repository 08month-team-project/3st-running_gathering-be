package com.runto.domain.admin.api;

import com.runto.domain.admin.application.AdminService;
import com.runto.domain.admin.dto.MonthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats/user")
    public ResponseEntity<List<MonthUserResponse>> getUserByMonth() {
        List<MonthUserResponse> responses = adminService.getUserByMonth();
        return ResponseEntity.ok(responses);
    }

}
