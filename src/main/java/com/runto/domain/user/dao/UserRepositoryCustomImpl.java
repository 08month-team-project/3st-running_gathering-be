package com.runto.domain.user.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.admin.dto.UserCountResponse;
import com.runto.domain.admin.type.AdminStatsCount;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runto.domain.user.domain.QBlackList.blackList;
import static com.runto.domain.user.domain.QUser.user;
import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.domain.user.type.UserStatus.DISABLED;

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

    private BooleanExpression userStateCondition(UserStatus status) {

        return switch (status) {
            case ACTIVE -> user.status.eq(ACTIVE);
            case DISABLED -> user.status.eq(DISABLED);
            default -> Expressions.TRUE.isTrue();
        };

    }

    private BooleanExpression userTypeCondition(AdminStatsCount type) {
        BooleanExpression notDisabled = user.status.ne(DISABLED);
        return switch (type) {
            case TOTAL -> notDisabled;
            case REPORTED -> notDisabled.and(user.reportCount.gt(0)).and(user.status.eq(ACTIVE));
            case BLACKLIST -> notDisabled.and(blackList.user.id.isNotNull());
        };
    }

}
