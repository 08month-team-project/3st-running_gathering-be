package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GatheringTimeStatus {

    ENDED("종료된 모임"),
    ONGOING("종료되지 않은 모임");

    private final String description;

}
