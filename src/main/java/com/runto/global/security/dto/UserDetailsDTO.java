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
public class UserDetailsDTO {

    private Long userId;
    private String name;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private String status;
    private String role;

    public static UserDetailsDTO of(User user) {
        return UserDetailsDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .gender(user.getGender().name())
                .status(user.getStatus().name())
                .password(user.getLocalAccount().getPassword())
                .role(user.getRole().name())
                .build();
    }
}
