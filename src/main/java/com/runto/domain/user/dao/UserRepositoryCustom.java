package com.runto.domain.user.dao;

import com.runto.domain.admin.dto.MonthUserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {

    List<MonthUserResponse>findAllByUserByMonth();

}
