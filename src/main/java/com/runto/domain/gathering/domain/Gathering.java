package com.runto.domain.gathering.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.dto.GatheringMember;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.*;
import com.runto.domain.image.domain.GatheringImage;
import com.runto.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.runto.domain.gathering.type.EventRequestStatus.APPROVED;
import static com.runto.domain.gathering.type.GatheringMemberRole.*;
import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;
import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.global.exception.ErrorCode.*;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "gatherings")
@Entity
public class Gathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    // 주최자 닉네임 반정규화는 보류
    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, name = "appointed_at")
    private LocalDateTime appointedAt;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunningConcept concept;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_distance")
    private GoalDistance goalDistance;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(nullable = false)
    private Long hits;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    private GatheringStatus status;

    @Column(name = "max_number", nullable = false)
    private Integer maxNumber;

    @Column(name = "current_number", nullable = false)
    private Integer currentNumber;

    @Column(name = "gathering_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GatheringType gatheringType;

    // TODO: 기존에 구현한 조회기능에 해당 필드에 대한 로직 추가
    @Column(name = "is_normal_completed", nullable = false)
    private Boolean isNormalCompleted;

    @JoinColumn(name = "event_gathering_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EventGathering eventGathering;

//    @Version
//    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringImage> contentImages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringMember> gatheringMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringViewRecord> gatheringViewRecords = new ArrayList<>();


    // 양방향관계,영속성전이를 통한 저장방식, 엔티티에서 dto를 참조하지 않는 구조를 고려하여 만들었음
    public void addContentImages(List<GatheringImage> contentImages) {
        if (contentImages == null || contentImages.size() < 1) {
            return;
        }
        if (contentImages.size() > 3) {
            throw new GatheringException(IMAGE_SAVE_LIMIT_EXCEEDED);
        }

        contentImages.forEach(image -> {
            image.assignGathering(this);
            this.contentImages.add(image);
        });
    }

    public void addGatheringViewRecord(Long userId) {
        gatheringViewRecords.add(new GatheringViewRecord(this, userId));
        increaseHits();
    }

    public void increaseHits() {
        hits++;
    }

    public void addMember(User user, GatheringMemberRole role) {

        validateAddMember(user, role);

        gatheringMembers.add(GatheringMember.of(this, user, role));
        //increaseCurrentNumber();
    }

    public void updateCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    private void validateAddMember(User user, GatheringMemberRole role) {
        // 정상 유저만 참여가능
        if (!ACTIVE.equals(user.getStatus())) {
            throw new GatheringException(USER_INACTIVE);
        }

        // 마감날짜
        if (LocalDateTime.now().isAfter(this.getDeadline())) {
            throw new GatheringException(PASSED_GATHERING_DEADLINE);
        }

        // NORMAL 상태 모임에만 참가 가능
        if (!NORMAL.equals(this.getStatus())) {
            throw new GatheringException(INVALID_PARTICIPATE_NOT_NORMAL_GATHERING);
        }

        // 이벤트모임의 경우 주최자 외에는 승인된 모임만 참가가능
        if (this.getEventGathering() != null &&
                !APPROVED.equals(this.getEventGathering().getStatus()) &&
                !ORGANIZER.equals(role)) {

            throw new GatheringException(INVALID_PARTICIPATE_NOT_APPROVED_EVENT);
        }
    }

    public void increaseCurrentNumber() {
        if (currentNumber == null) currentNumber = 0;
        currentNumber++;
    }

    public void decreaseCurrentNumber() {
        currentNumber--;
    }

    // 해당 모임을 이벤트모임으로 신청
    public void applyForEvent() {
        if (maxNumber < 10 || maxNumber > 300) {
            throw new GatheringException(EVENT_GATHERING_MAX_NUMBER);
        }
        this.eventGathering = new EventGathering(this);
    }

    public void updateNormalComplete(Long userId) {
        isNormalCompleted = true;
    }


    @PrePersist
    public void prePersist() {
        hits = 0L;
        isNormalCompleted = false;
    }


    public void validateBeforeCompletion(Long userId) {

        if (!Objects.equals(userId, this.organizerId)) {
            throw new GatheringException(INVALID_COMPLETE_GATHERING_NOT_ORGANIZER);
        }
        if (this.isNormalCompleted) {
            throw new GatheringException(ALREADY_NORMAL_COMPLETE_GATHERING);
        }
        if (!NORMAL.equals(this.status)) {
            throw new GatheringException(INVALID_COMPLETE_GATHERING_NOT_NORMAL_GATHERING);
        }
        if (this.appointedAt.isAfter(LocalDateTime.now())) {
            throw new GatheringException(INVALID_COMPLETE_GATHERING_BEFORE_MEETING);
        }
        if (this.appointedAt.plusDays(7).isBefore(LocalDateTime.now())) {
            throw new GatheringException(INVALID_COMPLETE_AFTER_ONE_WEEK);
        }
    }
}

