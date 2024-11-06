package com.runto.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeactivateUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private LocalDateTime deletedAt;

    public static DeactivateUser from(String email) {
        return DeactivateUser.builder()
                .email(email)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
