package com.runto.domain.image.dao;

import com.runto.domain.image.domain.GatheringImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringImageRepository extends JpaRepository<GatheringImage, Long> {

    List<GatheringImage> findGatheringImagesByGatheringId(Long gatheringId);
}
