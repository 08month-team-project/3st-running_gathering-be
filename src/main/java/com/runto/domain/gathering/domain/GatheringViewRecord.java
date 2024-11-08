package com.runto.domain.gathering.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "gathering_view_record")
@Entity
public class GatheringViewRecord {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "view_record_id")
    private Long id;

    // ID 간접참조로 설정하였음
    private Long gatheringId;

    private Long userId;

    @Column(updatable = false, name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public GatheringViewRecord(Long gatheringId, Long userId) {
        this.gatheringId = gatheringId;
        this.userId = userId;
    }
}
