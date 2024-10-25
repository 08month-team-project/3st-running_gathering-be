package com.runto.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    IMAGE_SAVE_LIMIT_EXCEEDED(BAD_REQUEST, "이미지 저장 허용 개수를 넘었습니다.")

    ,CHATROOM_ALREADY_EXIST(CONFLICT,"이미 존재하는 채팅방입니다.")
    ,CHATROOM_NOT_FOUND(NOT_FOUND,"존재하지 않는 채팅방입니다.")
    ,CHATROOM_ALREADY_JOINED(BAD_REQUEST,"이미 참여중인 채팅방입니다.")
    ,CHATROOM_FULL(BAD_REQUEST,"채팅방이 최대 인원수에 도달했습니다.")

    ;



    private final HttpStatus httpStatus;
    private final String message;
}

