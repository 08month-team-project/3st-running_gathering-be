package com.runto.domain.gathering.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.gathering.type.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.runto.domain.gathering.dto.QGatheringMember.gatheringMember;
import static com.runto.domain.gathering.type.AttendanceStatus.ATTENDING;

@RequiredArgsConstructor
@Repository
public class GatheringMemberRepositoryCustomImpl implements GatheringMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    @Override
    public long updateAttendanceAndDistance(Long gatheringId,
                                            AttendanceStatus status,
                                            Double realDistance,
                                            List<Long> memberIds) {

        return jpaQueryFactory
                .update(gatheringMember)
                .set(gatheringMember.attendanceStatus, status)
                .set(gatheringMember.realDistance, realDistance)
                .where(attendanceMemberCondition(status, memberIds),
                        gatheringMember.gathering.id.eq(gatheringId))
                .execute();
    }

    @Override
    public Long countMembers(Long gatheringId, AttendanceStatus status) {

        return jpaQueryFactory
                .select(gatheringMember.count())
                .from(gatheringMember)
                .where(gatheringMember.attendanceStatus.eq(status),
                        gatheringMember.gathering.id.eq(gatheringId))
                .fetchOne();
    }

    private BooleanExpression attendanceMemberCondition(AttendanceStatus status,
                                                        List<Long> memberIds) {

        if (ATTENDING.equals(status) && memberIds != null) {
            return gatheringMember.id.in(memberIds);

        } else {
            return gatheringMember.attendanceStatus.ne(ATTENDING);
        }
    }

}
