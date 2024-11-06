package com.runto.domain.gathering.dao;


import com.runto.domain.admin.dao.GatheringMgmtRepositoryCustom;
import com.runto.domain.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom, GatheringMgmtRepositoryCustom {

}
