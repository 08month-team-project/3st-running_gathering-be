package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.EventGathering;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventGatheringRepository extends JpaRepository<EventGathering, Long> {

    @Query("select e from EventGathering e " +
            " join fetch e.gathering g" +
            " where g.organizerId = :user_id")
    Slice<EventGathering> findEventGatheringsByUserId(
            @Param("user_id") Long userId, Pageable pageable);
}
