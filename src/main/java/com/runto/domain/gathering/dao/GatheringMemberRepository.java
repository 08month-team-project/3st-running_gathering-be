package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.dto.GatheringMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringMemberRepository extends JpaRepository<GatheringMember, Long> {

    @Query("select gm from GatheringMember gm " +
            " join fetch gm.user " +
            " where gm.gathering.id = :gathering_id")
    Slice<GatheringMember> findGatheringMembersByGatheringId(
            @Param("gathering_id") Long gatheringId, Pageable pageable);
}
