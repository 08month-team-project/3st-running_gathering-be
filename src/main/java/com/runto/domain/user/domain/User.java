package com.runto.domain.user.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import com.runto.domain.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    //TODO 추후 비속어필터 적용예정.
    @Column(nullable = false, length = 15, unique = true)
    private String nickname;

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

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id")
    private LocalAccount localAccount;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth2_id")
    private OAuth2 oAuth2;

    @Column(name = "report_count")
    private Long reportCount;

    @PrePersist
    public void prePersist() {
        status = UserStatus.ACTIVE;
    }

    public static User of(String email,String nickname,String password,String oAuth2Key) {
         User user = User.builder()
                .nickname(nickname)
                .email(email)
                .gender(Gender.NONE)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
         user.localAccount = LocalAccount.builder()
                 .password(password)
                 .build();
         user.oAuth2 = OAuth2.builder()
                 .oAuth2Key(oAuth2Key)
                 .build();
        return user;
    }
}
