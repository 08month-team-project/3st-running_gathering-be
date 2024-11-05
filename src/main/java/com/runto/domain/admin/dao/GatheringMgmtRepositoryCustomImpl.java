package com.runto.domain.admin.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.EventListResponse;
import com.runto.domain.admin.dto.GatheringCountResponse;
import com.runto.domain.admin.dto.MonthlyEventCountResponse;
import com.runto.domain.admin.dto.MonthlyParticipantsResponse;
import com.runto.domain.admin.type.AdminEventCount;
import com.runto.domain.admin.type.AdminGatherStatsCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runto.domain.admin.type.AdminEventCount.EVENTS_PER_MONTH;
import static com.runto.domain.gathering.domain.QEventGathering.eventGathering;
import static com.runto.domain.gathering.domain.QGathering.gathering;
import static com.runto.domain.gathering.type.EventRequestStatus.APPROVED;
import static com.runto.domain.gathering.type.GatheringStatus.NORMAL;
import static com.runto.domain.gathering.type.GatheringType.EVENT;
import static com.runto.domain.user.domain.QUser.user;
import static com.runto.domain.user.domain.report.QReport.report;

@RequiredArgsConstructor
@Repository
public class GatheringMgmtRepositoryCustomImpl implements GatheringMgmtRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GatheringCountResponse> manageGathering(AdminGatherStatsCount statsCount) {
        Expression<?> selectField = getSelectField(statsCount);

        return queryFactory
                .select(Projections.constructor(GatheringCountResponse.class,
                        selectField,
                        gathering.count().as("count")))
                .from(gathering)
                .where(gathering.status.eq(NORMAL))
                .groupBy(selectField)
                .fetch();
    }

    @Override
    public List<?> getEventsPerMonth(AdminEventCount eventCount) {
        StringTemplate formatDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", gathering.createdAt, "%Y-%m"
        );

        Expression<?> selectDto = getSelectDto(eventCount, formatDate);

        JPAQuery<?> query = queryFactory
                .select(selectDto)
                .from(gathering)
                .join(eventGathering)
                .on(gathering.eventGathering.id.eq(eventGathering.id))
                .where(eventGathering.status.eq(APPROVED));

        // 조건에 따라 그룹화를 추가
        if (eventCount == EVENTS_PER_MONTH) {
            query.groupBy(formatDate);
        }

        return query.fetch();
    }

    @Override
    public Slice<EventListResponse> getPendingApprovalEventList(Pageable pageable) {
        List<EventListResponse> eventList = queryFactory.select(Projections.constructor(EventListResponse.class,
                        gathering.eventGathering.id,
                        gathering.title,
                        gathering.createdAt,
                        gathering.location.addressName.addressName,
                        eventGathering.status,
                        report.reportReason,
                        user.email))
                        .from(gathering)
                        .join(eventGathering)
                        .on(gathering.eventGathering.id.eq(eventGathering.id))
                        .join(user)
                        .on(user.id.eq(gathering.organizerId))
                        .leftJoin(report)
                        .on(report.user.id.eq(gathering.organizerId))
                        .where(gathering.gatheringType.eq(EVENT))
                        .orderBy(gathering.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();

        return new SliceImpl<>(eventList, pageable, hasNextPage(pageable, eventList));
    }

    private boolean hasNextPage(Pageable pageable, List<EventListResponse> eventList){
        if (eventList.size() > pageable.getPageSize()){
            eventList.remove(eventList.size() - 1);
            return true;
        }
        return false;
    }

    private Expression<?> getSelectDto(AdminEventCount eventCount, StringTemplate formatDate) {
        final StringExpression month = formatDate.as("month");

        return switch (eventCount) {
            case EVENTS_PER_MONTH -> Projections.constructor(MonthlyEventCountResponse.class,
                    month, gathering.count().as("count"));
            case EVENT_PARTICIPANTS_PER_MONTH -> Projections.constructor(MonthlyParticipantsResponse.class,
                    month, gathering.title, gathering.currentNumber.as("count"));
        };
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