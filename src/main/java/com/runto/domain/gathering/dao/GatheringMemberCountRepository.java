package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.GatheringMemberCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GatheringMemberCountRepository extends JpaRepository<GatheringMemberCount, Long> {

    // 별도로 @Transactional 안 붙이면 테스트코드에서, Query requires transaction be in progress, but no transaction is known to be in progress 가 떴음
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select gmc from GatheringMemberCount gmc " +
            " where gmc.gathering.id = :gatheringId ")
    Optional<GatheringMemberCount> findByGatheringIdWithPessimisticLock(
            @Param("gatheringId") Long gatheringId);

}
