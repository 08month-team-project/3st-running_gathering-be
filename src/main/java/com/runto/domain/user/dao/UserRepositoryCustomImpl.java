package com.runto.domain.user.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runto.domain.user.domain.QUser.user;

import static com.runto.domain.user.domain.report.QBlackList.blackList;
import static com.runto.domain.user.domain.report.QReport.report;
import static com.runto.domain.user.type.UserStatus.*;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MonthUserResponse> findAllByUserByMonth(UserStatus status) {

        StringTemplate formatDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", user.createdAt, "%Y-%m"
        );

        return queryFactory
                .select(Projections.constructor(MonthUserResponse.class,
                        formatDate.as("month"),
                        user.count().as("user_count")
                ))
                .from(user)
                .where(userStateCondition(status))
                .groupBy(user.createdAt.yearMonth())
                .fetch();
    }

    @Override
    public UserCountResponse countUsersByType(AdminStatsCount statsCount) {

        return queryFactory
                .select(Projections.constructor(UserCountResponse.class,
                        user.count().as("user_count")))
                .from(user)
                .leftJoin(blackList)
                .on(user.id.eq(blackList.user.id))
                .leftJoin(report)
                .on(user.id.eq(report.user.id))
                .where(userTypeCondition(statsCount))
                .fetchOne();
    }

    @Override
    public Page<PenaltyDetailsResponse> findAllByPenalties(UserStatus status, Pageable pageable) {

        List<PenaltyDetailsResponse> content = queryFactory
                .select(Projections.constructor(PenaltyDetailsResponse.class,
                        user.nickname,
                        user.email,
                        countExpression(status)))
                .from(user)
                .where(userStateCondition(status))
                .leftJoin(penaltyType(status)).on(penaltyByUserId(status)) // 조인 테이블과 조건 설정
                .groupBy(user.nickname, user.email)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(user.count())
                .from(user)
                .leftJoin(penaltyType(status)).on(penaltyByUserId(status))
                .where(userStateCondition(status));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userStateCondition(UserStatus status) {

        return switch (status) {
            case ACTIVE -> user.status.eq(ACTIVE);
            case REPORTED -> user.status.eq(REPORTED);
            case BANNED -> user.status.eq(BANNED);
            case DISABLED -> user.status.eq(DISABLED);
        };

    }

    private BooleanExpression userTypeCondition(AdminStatsCount type) {
        return switch (type) {
            case TOTAL -> user.status.ne(DISABLED);
            case REPORTED -> report.user.id.isNotNull();
            case BLACKLIST -> blackList.user.id.isNotNull();
        };
    }

    private Expression<Long> countExpression(UserStatus status) {
        return switch (status) {
            case ACTIVE, DISABLED -> null;
            case REPORTED -> report.id.count();
            case BANNED -> blackList.cumulativeNumber;

        };
    }

    private EntityPath<?> penaltyType(UserStatus status) {
        return switch (status) {
            case ACTIVE, DISABLED -> null;
            case REPORTED -> report;
            case BANNED -> blackList;
        };
    }

    private BooleanExpression penaltyByUserId(UserStatus status) {
        return switch (status) {
            case ACTIVE, DISABLED -> null;
            case REPORTED -> report.user.id.eq(user.id);
            case BANNED-> blackList.user.id.eq(user.id);
        };
    }

}
