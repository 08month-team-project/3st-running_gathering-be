package com.runto.domain.gathering.dao;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.type.GatheringMemberRole;
import com.runto.domain.gathering.type.GatheringOrderField;
import com.runto.domain.gathering.type.GatheringTimeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.runto.domain.common.SortUtil.getOrderSpecifier;
import static com.runto.domain.gathering.domain.QGathering.gathering;
import static com.runto.domain.gathering.dto.QGatheringMember.gatheringMember;
import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringTimeStatus.ENDED;
import static com.runto.domain.gathering.type.GatheringTimeStatus.ONGOING;

@RequiredArgsConstructor
@Repository
public class GatheringRepositoryCustomImpl implements GatheringRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Slice<Gathering> getUserGatherings(Long userId,
                                              Pageable pageable,
                                              UserGatheringsRequestParams request) {

        List<Gathering> gatherings = jpaQueryFactory.selectFrom(gathering)
                .join(gathering.gatheringMembers).fetchJoin()
                .where(
                        memberRoleCondition(userId, request.getMemberRole()),
                        timeCondition(request.getGatheringTimeStatus()),
                        gathering.gatheringType.eq(request.getGatheringType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 다음 페이지에 가져올 컨텐츠가 있는지 확인하기 위함
                .orderBy(orderCondition(request.getOrderBy(), request.getSortDirection()))
                .fetch();

        return new SliceImpl<>(gatherings, pageable, hasNextPage(pageable, gatherings));
    }

    private boolean hasNextPage(Pageable pageable, List<Gathering> gatherings) {
        boolean hasNext = false;
        if (gatherings.size() > pageable.getPageSize()) {
            hasNext = true;
            gatherings.remove(gatherings.size() - 1); // 확인용으로 가져왔던 컨텐츠는 삭제
        }
        return hasNext;
    }


    // TODO 출석체크 api 구현 후 조건 추가될 수도 있음
    private BooleanExpression timeCondition(GatheringTimeStatus gatheringTimeStatus) {

        // 약속모임날짜시간이 지나지 않은 모임만 노출
        if (ONGOING.equals(gatheringTimeStatus)) {
            return gathering.appointedAt.after(LocalDateTime.now());
        }
        // 약속모임날짜시간이 지난 모임만 노출
        if (ENDED.equals(gatheringTimeStatus)) {
            return gathering.appointedAt.before(LocalDateTime.now());
        }

        // 전체 노출
        return null;
    }

    private BooleanExpression memberRoleCondition(Long userId, GatheringMemberRole memberRole) {

        // 회원이 주최자인 모임
        if (ORGANIZER.equals(memberRole)) {
            return gathering.organizerId.eq(userId);
        }

        // 회원이 참가자인 모임
        return gathering.id.in(
                JPAExpressions.select(gathering.id)
                        .from(gatheringMember)
                        .where(gatheringMember.user.id.eq(userId)));
    }



    // PathBuilder 동적 표현식 활용 (이유는 pr에 작성)
    private OrderSpecifier<?> orderCondition(GatheringOrderField gatheringOrderField,
                                             Sort.Direction direction) {

        return getOrderSpecifier(Gathering.class,
                gatheringOrderField.getName(), direction);
    }


    // 이젠 사용하지 않는 메서드지만 위의 방식과 비교하시기 편하라고 남겨둡니다.
//    private OrderSpecifier<?> orderConditionDepreciate(GatheringOrder order) {
//
//        // 약속기한 임박순
//        if (APPOINTMENT_ASC.equals(order)) {
//            return gathering.appointedAt.asc();
//        }
//
//        // 모임생성 최신순
//        if (CREATED_DATE_DESC.equals(order)) {
//            return gathering.createdAt.desc();
//        }
//
//        // 디폴트 조건 (프론트과 논의 후 변경될 수 있음)
//        return gathering.appointedAt.asc();
//    }
}
