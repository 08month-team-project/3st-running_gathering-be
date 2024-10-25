package com.runto.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    IMAGE_SERVER_ERROR(INTERNAL_SERVER_ERROR, "이미지 처리 중 내부 오류가 발생했습니다."),
    S3_OBJECT_NOT_FOUND(NOT_FOUND, "S3 객체를 찾을 수 없습니다."),
    IMAGE_SAVE_LIMIT_EXCEEDED(BAD_REQUEST, "이미지 저장 허용 개수를 넘었습니다."),
    UNSUPPORTED_IMAGE_EXTENSION(BAD_REQUEST, "지원하는 이미지 확장자가 아닙니다."),
    INVALID_FILE(BAD_REQUEST, "파일이 없거나 이름이 비어 있습니다."),
    IMAGE_CONVERSION_FAILED(INTERNAL_SERVER_ERROR, "이미지 변환에 실패했습니다."),
    IMAGE_ORDER_MISMATCH(BAD_REQUEST, "요청한 이미지 개수와 순서 개수가 일치하지 않습니다."),
    INVALID_REPRESENTATIVE_IMAGE_INDEX(BAD_REQUEST, "대표 이미지 인덱스가 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

