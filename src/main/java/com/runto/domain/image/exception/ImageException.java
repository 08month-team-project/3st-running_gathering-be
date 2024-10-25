package com.runto.domain.image.exception;

import com.runto.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ImageException extends RuntimeException {

    private final ErrorCode errorCode;

    // 직접 던지는 예외용
    public ImageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ImageException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }


}
