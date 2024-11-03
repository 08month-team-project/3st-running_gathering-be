package com.runto.domain.admin.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AdminEventCount {

    EVENTS_PER_MONTH,
    EVENT_PARTICIPANTS_PER_MONTH

}
