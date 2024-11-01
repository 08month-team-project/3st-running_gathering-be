package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.type.AttendanceStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringMemberRepositoryCustom {


    long updateAttendanceAndDistance(Long gatheringId,
                                     AttendanceStatus status,
                                     Double realDistance,
                                     List<Long> memberIds);
}
