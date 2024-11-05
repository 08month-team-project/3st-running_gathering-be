package com.runto.domain.gathering.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.type.EventRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.runto.domain.gathering.type.EventRequestStatus.PENDING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Table(name = "event_gathering")
@Entity
public class EventGathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "event_gathering_id")
    private Long id;

    // oneToOne 양방향일 경우 주인이 아닌 쪽을 가져올때, LAZY 가 작동안하지만 EventGathering 를 가져올땐,
    // 항상 Gathering 을 가져오기해서 join fetch 를 쓸거고, 큰 상관은 없는 듯 하다.
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "eventGathering")
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

    public void updateStatus(EventRequestStatus newStatus) {
        if (this.status != newStatus) {
            this.status = newStatus;
        }
    }

}
