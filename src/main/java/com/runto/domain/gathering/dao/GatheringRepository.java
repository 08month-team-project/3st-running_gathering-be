package com.runto.domain.gathering.dao;


import com.runto.domain.admin.dao.GatheringMgmtRepositoryCustom;
import com.runto.domain.gathering.domain.Gathering;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom, GatheringMgmtRepositoryCustom {

    @Query("select g from Gathering g " +
            " left join fetch g.eventGathering " +
            " where g.id = :gatheringId ")
    Optional<Gathering> findGatheringWithEventById(@Param("gatheringId") Long gatheringId);



    // 찾았던 것에서는 PESSIMISTIC_READ 의 경우 쓰기가 막혀있다고 했었는데, 직접 적용해보니, 수정이 되기는 한다. (동시성제어가 안될뿐)
    // 결국 읽어올 때만 다른 트랜잭션이 동시에 읽을 수 없도록 하는 것 같다
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from Gathering g " +
            " left join fetch g.eventGathering " +
            " where g.id = :gatheringId ")
    Optional<Gathering> findByIdWithPessimisticLock(@Param("gatheringId") Long gatheringId);


//    @Modifying
//    @Query("UPDATE Gathering g SET g.hits = g.hits + 1 WHERE g.id = :gatheringId")
//    void increaseHits(@Param("gatheringId") Long gatheringId);
}
