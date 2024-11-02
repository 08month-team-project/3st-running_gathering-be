package com.runto.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NicknameRequest {

    // 회원가입 시 닉네임 조건에 패턴이 없어서 패스하였음
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;
}
