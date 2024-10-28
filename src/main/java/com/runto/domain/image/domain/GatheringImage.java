package com.runto.domain.image.domain;

import com.runto.domain.common.BaseTimeEntity;
import com.runto.domain.gathering.domain.Gathering;
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
@Table(name = "gathering_image")
@Entity
public class GatheringImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "gathering_image_id")
    private Long id;

    @JoinColumn(name = "gathering_id")
    @ManyToOne(fetch = LAZY)
    private Gathering gathering;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "image_order",nullable = false)
    private Integer imageOrder;

//    @Column(name = "is_thumbnail", nullable = false)
//    private Boolean isThumbnail;


    public void assignGathering(Gathering gathering) {
        this.gathering = gathering;
    }

}
