package com.runto.global.security.dto;

import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    private Long userId;
    private String name;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private String role;
    private String oAuth2Key;

    public static UserDetailsDTO of(User user) {
        if (user.getLocalAccount() != null) {
            return UserDetailsDTO.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .gender(user.getGender().name())
                    .password(user.getLocalAccount().getPassword())
                    .role(user.getRole().name())
                    .build();
        }
        if (user.getOAuth2() != null) {
            return UserDetailsDTO.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .gender(user.getGender().name())
                    .oAuth2Key(user.getOAuth2().getOAuth2Key())
                    .role(user.getRole().name())
                    .build();
        }
        return null;
    }
}
