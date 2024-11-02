package com.runto.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileResponse {

    private String email;
    //private String name;
    private String nickname;
    private Gender gender;
    private UserStatus status;
    private LocalDateTime singupAt;
    private String profileUrl;


    public static UserProfileResponse from(User user) {
        return UserProfileResponse
                .builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .status(user.getStatus())
                .singupAt(user.getCreatedAt())
                .profileUrl(user.getProfileImageUrl())
                .build();
    }
}
