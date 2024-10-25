package com.runto.domain.gathering.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.exception.GatheringException;
import com.runto.domain.gathering.type.GatheringStatus;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
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

import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;
import static com.runto.global.exception.ErrorCode.*;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "gathering_type")
@Table(name = "gatherings")
@Entity
public class Gathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User host;

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

    @Builder.Default
    @OneToMany(mappedBy = "gathering", cascade = CascadeType.PERSIST)
    private List<GatheringImage> contentImages = new ArrayList<>();


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


    @PrePersist
    public void prePersist() {
        hits = 0L;
        status = NORMAL;
        currentNumber = 1; // 주최자에 대한 수
    }


}

