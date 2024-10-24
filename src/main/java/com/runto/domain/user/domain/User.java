package com.runto.domain.user.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import com.runto.domain.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users", indexes = {
        @Index(name = "idx_status_created_at", columnList = "user_status, created_at")
})
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Gender gender;

    @Column(nullable = false, name = "user_status")
    @Enumerated(STRING)
    private UserStatus status;

    @Column(nullable = false, name = "user_role")
    @Enumerated(STRING)
    private UserRole role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @PrePersist
    public void prePersist() {
        status = UserStatus.ACTIVE;
    }
}
