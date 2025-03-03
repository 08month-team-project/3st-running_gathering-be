package com.runto.domain.gathering.dto;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.AttendanceStatus;
import com.runto.domain.gathering.type.GatheringMemberRole;
import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static com.runto.domain.gathering.type.AttendanceStatus.ATTENDING;

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

    @Column(name = "real_distance")
    private Double realDistance;

    public static GatheringMember of(Gathering gathering, User user, GatheringMemberRole role) {
        return GatheringMember.builder()
                .gathering(gathering)
                .user(user)
                .role(role)
                .build();
    }

    public void checkAttendance(AttendanceStatus status, double realDistance) {

        if (status != null) {
            this.attendanceStatus = status;
        } else {
            return;
        }

        if (ATTENDING.equals(status)) {
            this.realDistance = realDistance;
        }

    }

    @PrePersist
    public void prePersist() {
        attendanceStatus = AttendanceStatus.PENDING;
        realDistance = 0.0;
    }

}
