package com.runto.domain.gathering.dao;


import com.runto.domain.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Query("select g from Gathering g " +
            " join fetch g.gatheringMembers gm " +
            " join fetch gm.user " +
            " where g.id= :gathering_id ")
    Optional<Gathering> findGatheringDetailById(@Param("gathering_id") Long gatheringId);
}
