package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.type.AttendanceStatus;
import com.runto.domain.gathering.type.GatheringMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class GatheringMemberResponse {

    private Long memberId;
    private Long memberAccountId;
    private String nickname;
    private String profileImageUrl;
    private GatheringMemberRole role;
    private AttendanceStatus attendanceStatus;
    private Double realDistance;

//    public static List<GatheringMemberResponse> from(List<GatheringMember> gatheringMembers) {
//
//        return gatheringMembers.stream()
//                .map(GatheringMemberResponse::from)
//                .toList();
//    }

    public static GatheringMemberResponse from(GatheringMember gatheringMember) {

        return GatheringMemberResponse.builder()
                .memberId(gatheringMember.getId())
                .memberAccountId(gatheringMember.getUser().getId())
                .nickname(gatheringMember.getUser().getNickname())
                .profileImageUrl(gatheringMember.getUser().getProfileImageUrl())
                .role(gatheringMember.getRole())
                .attendanceStatus(gatheringMember.getAttendanceStatus())
                .realDistance(gatheringMember.getRealDistance())
                .build();
    }

}
