package com.runto.domain.admin.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AdminStatsCount {

    TOTAL,
    BLACKLIST;

}
