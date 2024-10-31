package com.runto.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.dto.GatheringMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.runto.domain.gathering.type.AttendanceStatus.ATTENDING;
import static com.runto.domain.gathering.type.GatheringType.EVENT;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCalenderResponse {

    private int monthlyGatheringTotalCount;
    private int attendanceCount;
    private int eventGatheringCount;
    private double totalRealDistance;

    private List<CalendarGatheringResponse> gatherings;


    public static UserCalenderResponse fromGatheringMembers(List<GatheringMember> gatheringMembers) {

        UserCalenderResponse response = new UserCalenderResponse();
        response.monthlyGatheringTotalCount = gatheringMembers.size();

        response.gatherings = new ArrayList<>();

        for (GatheringMember gatheringMember : gatheringMembers) {
            response.gatherings.add(CalendarGatheringResponse.from(gatheringMember));

            response.totalRealDistance += gatheringMember.getRealDistance();

            if (ATTENDING.equals(gatheringMember.getAttendanceStatus())){
                response.attendanceCount++;
            }

            if(EVENT.equals(gatheringMember.getGathering().getGatheringType())){
                response.eventGatheringCount++;
            }
        }

        return response;
    }
}
