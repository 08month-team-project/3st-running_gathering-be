package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.dto.GatheringMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringMemberRepository extends JpaRepository<GatheringMember, Long>, GatheringMemberRepositoryCustom {

    @Query("select gm from GatheringMember gm " +
            " join fetch gm.user " +
            " where gm.gathering.id = :gathering_id")
    Slice<GatheringMember> findGatheringMembersWithUserByGatheringId(
            @Param("gathering_id") Long gatheringId, Pageable pageable);

    List<GatheringMember> findGatheringMembersByGatheringId(Long gatheringId);

    @Query("select count(gm) > 0 from GatheringMember gm " +
            "where gm.gathering.id = :gathering_id and gm.user.id =:userId")
    boolean existsGatheringMemberByGatheringIdAndUserId(@Param("gathering_id") Long gatheringId,
                                     @Param("user_id") Long userId);
}
