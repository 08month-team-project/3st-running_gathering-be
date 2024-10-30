package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.EventGathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventGatheringRepository extends JpaRepository<EventGathering, Long> {
}
