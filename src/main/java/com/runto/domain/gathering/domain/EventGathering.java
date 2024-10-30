package com.runto.domain.gathering.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.type.EventRequestStatus;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static com.runto.domain.gathering.type.EventRequestStatus.PENDING;
import static jakarta.persistence.GenerationType.IDENTITY;


@NoArgsConstructor
@Entity
public class EventGathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "gathering_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Gathering gathering;

    @Enumerated(EnumType.STRING)
    private EventRequestStatus status;

    @PrePersist
    public void prePersist() {
        status = PENDING;
    }

    public EventGathering(Gathering gathering) {
        this.gathering = gathering;
    }
}
