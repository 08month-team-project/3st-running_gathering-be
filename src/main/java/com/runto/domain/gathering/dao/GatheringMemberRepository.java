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
            " where gm.gathering.id = :gatheringId")
    Slice<GatheringMember> findGatheringMembersWithUserByGatheringId(
            @Param("gatheringId") Long gatheringId, Pageable pageable);

    List<GatheringMember> findGatheringMembersByGatheringId(Long gatheringId);


    @Query("select count(gm) > 0 from GatheringMember gm " +
            "where gm.gathering.id = :gatheringId and gm.user.id =:userId")
    boolean existsGatheringMemberByGatheringIdAndUserId(
            @Param("gatheringId") Long gatheringId, @Param("userId") Long userId);


    boolean existsByGatheringIdAndUserId(Long GatheringId, Long userId);

    void deleteByGatheringIdAndUserId(Long gatheringId, Long userId);

    int countByGatheringId(Long GatheringId);
}
