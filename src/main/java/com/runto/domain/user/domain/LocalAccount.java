package com.runto.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String password;

    public static LocalAccount from(User user, String password) {
        return LocalAccount.builder()
                .user(user)
                .password(password)
                .build();
    }
}
