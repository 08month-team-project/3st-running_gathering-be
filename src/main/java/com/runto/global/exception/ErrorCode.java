package com.runto.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    IMAGE_SAVE_LIMIT_EXCEEDED(BAD_REQUEST, "이미지 저장 허용 개수를 넘었습니다."),

    ALREADY_EXIST_USER(CONFLICT,"이미 존재하는 사용자입니다."),
    USER_NOT_FOUND(NOT_FOUND,"존재하지 않는 유저입니다." );
    private final HttpStatus httpStatus;
    private final String message;
}

