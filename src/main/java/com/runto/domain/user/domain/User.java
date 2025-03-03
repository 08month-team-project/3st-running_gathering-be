package com.runto.domain.user.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.user.excepction.UserException;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import com.runto.domain.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.global.exception.ErrorCode.INVALID_PROFILE_UPDATE_INACTIVE_USER;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id")
    private LocalAccount localAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "o_auth2_id")
    private OAuth2 oAuth2;

    public static User of(String email, String nickname) {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .gender(Gender.NONE)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
    }

    public User withLocalAccount(String password) {
        this.localAccount = LocalAccount.builder()
                .password(password)
                .build();
        return this;
    }

    public User withOAuth2(String oAuth2Key) {
        this.oAuth2 = OAuth2.builder()
                .oAuth2Key(oAuth2Key)
                .build();
        return this;
    }

    public void releaseUser() {
        this.status = UserStatus.ACTIVE;
    }

    public void updateNickname(String nickname) {

        if (!ACTIVE.equals(this.status)) {
            throw new UserException(INVALID_PROFILE_UPDATE_INACTIVE_USER);
        }
        this.nickname = nickname;
    }

    public void updateProfile(String profileUrl) {
        this.profileImageUrl = profileUrl;
    }

    @PrePersist
    public void prePersist() {
        status = UserStatus.ACTIVE;
    }

    public void disabledUser(){
        status = UserStatus.DISABLED;
    }
}
