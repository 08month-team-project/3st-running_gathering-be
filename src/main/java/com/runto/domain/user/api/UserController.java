package com.runto.domain.user.api;

import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.dto.SignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    //model attribute 사용시 setter필요
@PostMapping("/signup")
@Operation(summary = "회원가입")
public ResponseEntity<SignupResponse> signup(@ModelAttribute SignupRequest signupRequest) {
    return ResponseEntity.ok(userService.createUser(signupRequest));
}
}
