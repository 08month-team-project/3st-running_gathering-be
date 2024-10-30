package com.runto.domain.user.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dto.CheckEmailRequest;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.dto.UserCalenderResponse;
import com.runto.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @GetMapping("/calender")
    public ResponseEntity<UserCalenderResponse> getMyMonthlyGatherings(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(gatheringService
                .getUserMonthlyGatherings(userDetails.getUserId(), year, month));
    }
}
