package com.runto.domain.user.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.UserEventGatheringsResponse;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.dto.UserGatheringsResponse;
import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dto.CheckEmailRequest;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.dto.UserCalenderResponse;
import com.runto.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final GatheringService gatheringService;


    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.createUser(signupRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 체크")
    @PostMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@Valid @RequestBody CheckEmailRequest checkEmailRequest) {
        userService.checkEmailDuplicate(checkEmailRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 런닝캘린더 조회")
    @GetMapping("/calender")
    public ResponseEntity<UserCalenderResponse> getMyMonthlyGatherings(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(gatheringService
                .getUserMonthlyGatherings(userDetails.getUserId(), year, month));
    }

    @Operation(summary = "내 모임목록 조회 (일반모임, 이벤트모임)")
    @GetMapping("/gatherings")
    public ResponseEntity<UserGatheringsResponse> getMyGatherings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 8) Pageable pageable,
            @Valid @ModelAttribute UserGatheringsRequestParams requestParams) {

        return ResponseEntity.ok(gatheringService
                .getUserGatherings(userDetails.getUserId(), pageable, requestParams));
    }

    @Operation(summary = "내 이벤트 신청목록 조회")
    @GetMapping("/events")
    public ResponseEntity<UserEventGatheringsResponse> getMyEventRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 8) Pageable pageable) {

        return ResponseEntity.ok(gatheringService
                .getUserEventRequests(userDetails.getUserId(), pageable));
    }

    //TODO 회원탈퇴
    //1) 탈퇴한 회원테이블 따로 만들기 이메일중복방지
    //2) 탈퇴 시 이메일 더미이메일로 교체? (별로선호하지않으나 쉬울듯.)
}
