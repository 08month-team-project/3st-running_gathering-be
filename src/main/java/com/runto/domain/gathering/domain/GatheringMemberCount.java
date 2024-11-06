package com.runto.domain.gathering.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity
public class GatheringMemberCount {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "gathering_member_count_id")
    private Long gatheringMemberCountId;

    @JoinColumn(name = "gathering_id")
    @OneToOne(fetch = LAZY)
    private Gathering gathering;

    @Column(name = "max_number", nullable = false)
    private Integer maxNumber;

    @Column(name = "current_number", nullable = false)
    private Integer currentNumber;

    public static GatheringMemberCount from(Gathering gathering) {

        return GatheringMemberCount.builder()
                .gathering(gathering)
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .build();
    }

    public void increaseCurrentMember() {
        if(currentNumber == null) currentNumber = 0;
        currentNumber++;
    }
}
