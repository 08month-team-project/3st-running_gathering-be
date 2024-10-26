package com.runto.domain.user.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.admin.dto.MonthUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runto.domain.user.domain.QUser.user;
import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.domain.user.type.UserStatus.DISABLED;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MonthUserResponse> findAllByUserByMonth() {

        StringTemplate formatDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", user.createdAt, "%Y-%m"
        );

        return queryFactory
                .select(Projections.constructor(MonthUserResponse.class,
                        formatDate.as("month"),
                        new CaseBuilder()
                                .when(user.status.eq(ACTIVE)).then(1).otherwise(0).sum().as("active_user_count"),
                        new CaseBuilder()
                                .when(user.status.eq(DISABLED)).then(1).otherwise(0).sum().as("deactivated_user_count")
                ))
                .from(user)
                .groupBy(user.createdAt.yearMonth())
                .fetch();
    }
}
