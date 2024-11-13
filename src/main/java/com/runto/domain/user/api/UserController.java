package com.runto.domain.user.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.UserEventGatheringsResponse;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.dto.UserGatheringsResponse;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dto.*;
import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final GatheringService gatheringService;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JWTUtil jwtUtil;


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
    @GetMapping("/gatherings/events")
    public ResponseEntity<UserEventGatheringsResponse> getMyEventRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 8) Pageable pageable) {

        return ResponseEntity.ok(gatheringService
                .getUserEventRequests(userDetails.getUserId(), pageable));
    }

    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/{user_id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable("user_id") Long userId) {

        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @Operation(summary = "내 닉네임 수정")
    @PatchMapping("/profile-nickname")
    public ResponseEntity<Void> updateMyNickname(
            @Valid @RequestBody NicknameRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        userService.updateUserNickname(request.getNickname(), userDetails.getUserId());
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "내 프로필사진 수정")
    @PatchMapping("/profile-image")
    public ResponseEntity<ImageUrlDto> updateMyProfileImage(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(userService.
                updateUserProfile(userDetails.getUserId(), image));
    }

    @Operation(summary = "회원 탈퇴")
    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivateUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deactivateUser(userDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disabled")
    public String firstApi(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("disabledUserJob"), jobParameters);
        return "ok";
    }

    @GetMapping("/cookie")
    public ResponseEntity<CookieResponse> sendCookie(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.error("cookie is null");
            return null;
        }
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {
                CookieResponse cookieResponse = userService.token(userDetails,cookie.getValue());
                return ResponseEntity.ok(cookieResponse);
            }
        }
        return null;
    }
}
