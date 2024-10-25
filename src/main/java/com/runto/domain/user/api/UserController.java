package com.runto.domain.user.api;

import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    //model attribute 사용시 setter필요
@PostMapping("/signup")
@Operation(summary = "회원가입")
public ResponseEntity<Void> signup(@ModelAttribute @Valid SignupRequest signupRequest) {
    userService.createUser(signupRequest);
    return ResponseEntity.ok().build();
}
}
