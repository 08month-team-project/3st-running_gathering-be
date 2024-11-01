package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GatheringStatus {
    NORMAL,   // 정상
    DELETED,  // 삭제
    REPORTED, // 신고

}
