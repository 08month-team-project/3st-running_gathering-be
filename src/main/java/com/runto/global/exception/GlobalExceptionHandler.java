package com.runto.global.exception;

import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.image.exception.ImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 미처 잡지 못한 에러 처리용
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        log.error("[Exception] ex", e);
        return ResponseEntity.internalServerError().body("서버에 문제가 발생했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] {}", FieldErrorCustom.getFieldErrorList(e.getFieldErrors()));
        return ResponseEntity.badRequest().body(new ErrorResult(e));
    }

    @ExceptionHandler(GatheringException.class)
    public ResponseEntity<ErrorResult> handleGatheringException(GatheringException e) {
        return makeErrorResult(e.getErrorCode());
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResult> handleImageException(ImageException e) {
        log.error("[ImageException] ex", e);
        return makeErrorResult(e.getErrorCode());
    }


    private ResponseEntity<ErrorResult> makeErrorResult(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResult(errorCode));
    }

}
