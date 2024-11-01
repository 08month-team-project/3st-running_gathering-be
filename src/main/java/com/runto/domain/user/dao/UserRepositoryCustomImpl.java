package com.runto.domain.user.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.domain.report.QBlackList;
import com.runto.domain.user.domain.report.QReport;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
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
                .where(userTypeCondition(statsCount))
                .fetchOne();
    }

    @Override
    public List<PenaltyDetailsResponse> findAllByPenalties(UserStatus status) {
        QReport reportAlias = QReport.report; // Q타입을 사용하여 report를 alias로 생성
        QBlackList blackListAlias = QBlackList.blackList; // Q타입을 사용하여 blackList를 alias로 생성

        // 집계 표현식 및 조인 테이블 결정
        Expression<Long> countExpression = (status == REPORTED) ? reportAlias.id.count() : blackListAlias.cumulativeNumber;
        EntityPath<?> joinTable = (status == REPORTED) ? reportAlias : blackListAlias; // 조인 테이블 타입
        BooleanExpression joinCondition = (status == REPORTED) ? reportAlias.user.id.eq(user.id) : blackListAlias.user.id.eq(user.id);

        return queryFactory
                .select(Projections.constructor(PenaltyDetailsResponse.class,
                        user.nickname,
                        user.email,
                        countExpression))
                .from(user)
                .where(userStateCondition(status))
                .leftJoin(joinTable).on(joinCondition) // 조인 테이블과 조건 설정
                .groupBy(user.nickname, user.email)
                .fetch();
    }

    private BooleanExpression userStateCondition(UserStatus status) {

        return switch (status) {
            case ACTIVE -> user.status.eq(ACTIVE);
            case REPORTED -> user.status.eq(REPORTED);
            case BANNED -> user.status.eq(BANNED);
            case DISABLED -> user.status.eq(DISABLED);
            default -> Expressions.TRUE.isTrue();
        };

    }

    private BooleanExpression userTypeCondition(AdminStatsCount type) {
        BooleanExpression notDisabled = user.status.ne(DISABLED);
        return switch (type) {
            case TOTAL -> notDisabled;
            case BLACKLIST -> notDisabled.and(blackList.user.id.isNotNull());
        };
    }

}
