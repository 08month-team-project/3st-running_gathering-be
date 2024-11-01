package com.runto.domain.gathering.dao;


import com.runto.domain.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom {

    @Query("select g from Gathering g " +
            " left join fetch g.eventGathering " +
            " where g.id= :gathering_id ")
    Optional<Gathering> findGatheringById(@Param("gathering_id") Long gatheringId);
}
