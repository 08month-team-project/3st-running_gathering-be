package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.type.GatheringMemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class GatheringMemberResponse {

    private Long gatheringMemberId;
    private String nickname;
    private String profileImageUrl;
    private GatheringMemberRole role;

    public static List<GatheringMemberResponse> from(List<GatheringMember> gatheringMembers) {

        return gatheringMembers.stream()
                .map(GatheringMemberResponse::from)
                .toList();
    }

    public static GatheringMemberResponse from(GatheringMember gatheringMember) {

        return GatheringMemberResponse.builder()
                .gatheringMemberId(gatheringMember.getId())
                .nickname(gatheringMember.getUser().getNickname())
                .profileImageUrl(gatheringMember.getUser().getProfileImageUrl())
                .role(gatheringMember.getRole())
                .build();
    }

}
