package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.GatheringViewRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringViewRecordRepository extends JpaRepository<GatheringViewRecord, Long> {

    boolean existsByGatheringIdAndUserId(Long gatheringId, Long userId);
}
