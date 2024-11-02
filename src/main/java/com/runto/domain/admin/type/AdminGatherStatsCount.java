package com.runto.domain.admin.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AdminGatherStatsCount {

    GATHERING_BY_MONTH,
    GATHERING_BY_REGION,
    GATHERING_BY_DISTANCE,
    GATHERING_BY_CONCEPT

}
