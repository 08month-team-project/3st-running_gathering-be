package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AttendanceStatus {

    ATTENDING,   // 참석
    NOT_ATTENDING, // 불참
    PENDING      // 대기
}
