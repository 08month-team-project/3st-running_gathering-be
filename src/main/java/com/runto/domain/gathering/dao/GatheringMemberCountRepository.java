package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.GatheringMemberCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringMemberCountRepository extends JpaRepository<GatheringMemberCount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GatheringMemberCount> findByGatheringId(Long gatheringId);

}
