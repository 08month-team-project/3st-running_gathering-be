package com.runto.global.security.dto;

import com.runto.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2DetailsDTO {

    private Long userId;
    private String name;
    private String oauthKey;
    private String nickname;
    private String email;
    private String gender;
    private String status;
    private String role;

    public static OAuth2DetailsDTO of(User user) {
        return OAuth2DetailsDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .gender(user.getGender().name())
                .oauthKey(user.getOAuth2().getOAuth2Key())
                .role(user.getRole().name())
                .build();
    }
}
