package com.runto.domain.admin.dao;

import com.runto.domain.admin.dto.MonthlyGatheringCountResponse;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringMgmtRepositoryCustom {

    List<MonthlyGatheringCountResponse> manageGathering(AdminGatherStatsCount statsCount);
}
