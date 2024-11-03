package com.runto.domain.admin.dao;

import com.runto.domain.admin.dto.GatheringCountResponse;
import com.runto.domain.admin.dto.MonthlyEventCountResponse;
import com.runto.domain.admin.type.AdminEventCount;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringMgmtRepositoryCustom {

    List<GatheringCountResponse> manageGathering(AdminGatherStatsCount statsCount);

    List<?> getEventsPerMonth(AdminEventCount eventCount);
}
