package com.runto.domain.admin.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.MonthlyGatheringCountResponse;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runto.domain.gathering.domain.QGathering.gathering;
import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;

@RequiredArgsConstructor
@Repository
public class GatheringMgmtRepositoryCustomImpl implements GatheringMgmtRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MonthlyGatheringCountResponse> manageGathering(AdminGatherStatsCount statsCount) {
        Expression<?> selectField = getSelectField(statsCount);

        return queryFactory
                .select(Projections.constructor(MonthlyGatheringCountResponse.class,
                        selectField,
                        gathering.count().as("count")))
                .from(gathering)
                .where(gathering.status.eq(NORMAL))
                .groupBy(selectField)
                .fetch();
    }

    private Expression<?> getSelectField(AdminGatherStatsCount statsCount) {
        return switch (statsCount) {
            case GATHERING_BY_MONTH -> Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", gathering.createdAt);
            case GATHERING_BY_CONCEPT -> gathering.concept.stringValue();
            case GATHERING_BY_REGION -> gathering.location.addressName.region1DepthName;
            case GATHERING_BY_DISTANCE -> gathering.goalDistance.stringValue();
        };
    }
}
