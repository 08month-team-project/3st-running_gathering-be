package com.runto.domain.user.dao;

import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.type.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {

    List<MonthUserResponse>findAllByUserByMonth(UserStatus status);

    UserCountResponse countUsersByType(AdminStatsCount statsCount);

    Page<PenaltyDetailsResponse> findAllByPenalties(UserStatus status, Pageable pageable);
}
