package com.runto.domain.gathering.dao;

import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.GatheringMember;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatheringRepositoryCustom {

    Slice<Gathering> getUserGeneralGatherings(Long userId,
                                              Pageable pageable,
                                              UserGatheringsRequestParams requestParams);

    Slice<Gathering> getUserEventGatherings(Long userId,
                                            Pageable pageable,
                                            UserGatheringsRequestParams requestParams);

    List<GatheringMember> getUserMonthlyGatherings(Long userId, int year, int month);
}
