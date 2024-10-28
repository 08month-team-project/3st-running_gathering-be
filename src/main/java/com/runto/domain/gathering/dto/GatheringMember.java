package com.runto.domain.gathering.dto;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.AttendanceStatus;
import com.runto.domain.gathering.type.GatheringMemberRole;
import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GatheringMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "gathering_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Gathering gathering;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status")
    private AttendanceStatus attendanceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "gathering_member_role")
    private GatheringMemberRole role;

    public static GatheringMember of(Gathering gathering, User user, GatheringMemberRole role) {
        return GatheringMember.builder()
                .gathering(gathering)
                .user(user)
                .role(role)
                .build();
    }

    @PrePersist
    public void prePersist() {
        attendanceStatus = AttendanceStatus.PENDING;
    }

}
