package com.runto.domain.gathering.dao;


import com.runto.domain.admin.dao.GatheringMgmtRepositoryCustom;
import com.runto.domain.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom, GatheringMgmtRepositoryCustom {

    Optional<Gathering> findGatheringById(Long id);

    @Query("select g from Gathering g " +
            " left join fetch g.eventGathering " +
            " where g.id = :gatheringId ")
    Optional<Gathering> findGatheringWithEventById(@Param("gatheringId") Long gatheringId);

}
