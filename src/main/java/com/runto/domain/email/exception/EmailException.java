package com.runto.domain.email.exception;

import com.runto.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {

    private final ErrorCode errorCode;
    public EmailException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
