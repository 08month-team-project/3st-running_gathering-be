package com.runto.domain.gathering.dao;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.domain.QCoordinates;
import com.runto.domain.gathering.dto.*;
import com.runto.domain.gathering.type.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.ConstantImpl.create;
import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.runto.domain.common.SortUtil.getOrderSpecifier;
import static com.runto.domain.gathering.domain.QEventGathering.eventGathering;
import static com.runto.domain.gathering.domain.QGathering.gathering;
import static com.runto.domain.gathering.dto.QGatheringMember.gatheringMember;
import static com.runto.domain.gathering.type.EventRequestStatus.*;
import static com.runto.domain.gathering.type.GatheringMemberRole.ORGANIZER;
import static com.runto.domain.gathering.type.GatheringMemberRole.PARTICIPANT;
import static com.runto.domain.gathering.type.GatheringOrderField.APPOINTED_AT;
import static com.runto.domain.gathering.type.GatheringStatus.*;
import static com.runto.domain.gathering.type.GatheringTimeStatus.ENDED;
import static com.runto.domain.gathering.type.GatheringTimeStatus.ONGOING;
import static com.runto.domain.gathering.type.GatheringType.EVENT;
import static com.runto.domain.gathering.type.GatheringType.GENERAL;
import static com.runto.domain.gathering.type.ParticipationEligibility.AVAILABLE;
import static com.runto.domain.gathering.type.ParticipationEligibility.NOT_AVAILABLE;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RequiredArgsConstructor
@Repository
public class GatheringRepositoryCustomImpl implements GatheringRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    
    // TODO: 모든 모임글조회쿼리에 user 까지 패치조인 필요
    @Override
    public Slice<Gathering> getUserGeneralGatherings(Long userId,
                                                     Pageable pageable,
                                                     UserGatheringsRequestParams request) {

        List<Gathering> gatherings = jpaQueryFactory.selectFrom(gathering)
                .join(gathering.gatheringMembers).fetchJoin()
                .where(
                        memberRoleCondition(userId, request.getMemberRole()),
                        timeCondition(request.getGatheringTimeStatus()),
                        gatheringTypeCondition(GENERAL),
                        statusCondition(null)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 다음 페이지에 가져올 컨텐츠가 있는지 확인하기 위함
                .orderBy(orderCondition(request.getOrderBy(), request.getSortDirection()))
                .fetch();

        return new SliceImpl<>(gatherings, pageable, hasNextPage(pageable, gatherings));
    }

    /**
     * 양방향으로 바꾸면서 getUserGeneralGatherings 과 한개의 조건을 제외하고 완전히 똑같아서,
     * <p>
     * eventGathering.status 에 대한 condition을 따로 만들고(일반 모임은 null로 반환하면 되니까)
     * 두 메서드를 합쳐서 쓸까 했지만
     * 일반모임 조회일때도 eventGathering.status 에 대한 조건 로직이 들어가는게 뭔가 이상해보여서 그만두었음
     */
    @Override
    public Slice<Gathering> getUserEventGatherings(Long userId,
                                                   Pageable pageable,
                                                   UserGatheringsRequestParams request) {

        List<Gathering> gatherings = jpaQueryFactory.selectFrom(gathering)
                .join(gathering).fetchJoin()
                .where(
                        memberRoleCondition(userId, request.getMemberRole()),
                        timeCondition(request.getGatheringTimeStatus()),
                        gatheringTypeCondition(EVENT),
                        statusCondition(null),
                        eventStatusCondition(APPROVED)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(orderCondition(request.getOrderBy(), request.getSortDirection()))
                .fetch();

        return new SliceImpl<>(gatherings, pageable, hasNextPage(pageable, gatherings));
    }

    public List<GatheringMember> getUserMonthlyGatherings(Long userId, int year, int month) {

        // 주최자, 참가자인지 상관없이 유저가 속한 일반모임,이벤트모임 조회

        return jpaQueryFactory.selectFrom(gatheringMember)
                .join(gatheringMember.gathering, gathering).fetchJoin() // 조인 뒤 별칭을 지어주지않으면 에러발생 SemanticException (참조가 모호)
                .leftJoin(gathering.eventGathering, eventGathering).fetchJoin()
                .where(
                        yearMonthCondition(year, month),
                        gatheringMember.user.id.eq(userId),
                        gathering.status.ne(DELETED),
                        eventGathering.isNull()
                                .or(eventGathering.isNotNull()
                                        .and(eventGathering.status.eq(APPROVED)))
                )
                .orderBy(orderCondition(APPOINTED_AT, ASC))

                .fetch();
    }

    @Override
    public Slice<Gathering> getGeneralGatherings(Pageable pageable,
                                                 GatheringsRequestParams request) {


        List<Gathering> gatherings = jpaQueryFactory.selectFrom(gathering)
                .join(gathering.gatheringMembers).fetchJoin()
                .where(
                        gatheringTypeCondition(GENERAL),
                        participationCondition(request.getParticipationEligibility()), // 참가가능상태
                        searchTitleCondition(request.getSearchTitle()), // 제목검색
                        statusCondition(null), // 모임글 상태
                        goalDistanceCondition(request.getGoalDistance()), // 목표거리
                        runningConceptCondition(request.getRunningConcept()), // 러닝컨셉
                        radiusDistanceCondition(request.getGeoRadius()) // 좌표 기준 X km 반경
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 다음 페이지에 가져올 컨텐츠가 있는지 확인하기 위함
                .orderBy(orderCondition(request.getOrderBy(), request.getSortDirection()))
                .fetch();

        return new SliceImpl<>(gatherings, pageable, hasNextPage(pageable, gatherings));
    }


    private BooleanExpression gatheringTypeCondition(GatheringType type) {

        if (type != null) {
            return gathering.gatheringType.eq(type);
        }

        return null;
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
        if (PARTICIPANT.equals(memberRole)) {

            return gathering.id.in(
                    JPAExpressions.select(gathering.id)
                            .from(gatheringMember)
                            .where(gatheringMember.user.id.eq(userId)
                                    .and(gatheringMember.role.eq(PARTICIPANT))));
        }

        // 회원이 해당 모임의 멤버인 경우(주최자, 참가자 상관없이)
        return gathering.id.in(
                JPAExpressions.select(gathering.id)
                        .from(gatheringMember)
                        .where(gatheringMember.user.id.eq(userId)));
    }

    private BooleanExpression yearMonthCondition(int year, int month) {

        LocalDateTime localDateTime
                = LocalDateTime.of(year, month, 1, 0, 0);

        return gathering.appointedAt.after(localDateTime)
                .and(gathering.appointedAt.before(localDateTime.plusMonths(1)));
    }

    // 참가 가능여부 조건
    private BooleanExpression participationCondition(ParticipationEligibility participation) {

        // 참가 가능
        if (AVAILABLE.equals(participation)) {
            gathering.deadline.after(LocalDateTime.now())
                    .and(gathering.currentNumber.lt(gathering.maxNumber));
        }

        // 참가 불가
        if (NOT_AVAILABLE.equals(participation)) {
            gathering.deadline.before(LocalDateTime.now())
                    .or(gathering.currentNumber.goe(gathering.maxNumber));
        }

        // 상관없이 모두 노출
        return null;
    }

    private BooleanExpression statusCondition(GatheringStatus status) {

        if (status != null) {
            return gathering.status.eq(status);
        }

        // 디폴트 값 (삭제상태 외 모두 노출)
        return gathering.status.ne(DELETED);
    }

    // 이벤트모임의 상태
    private BooleanExpression eventStatusCondition(EventRequestStatus status) {

        if (status != null) {
            return gathering.eventGathering
                    .status.eq(status);
        }

        return null; // 모두 노출
    }

    // es 적용 못할 경우를 대비해서 적용
    private BooleanExpression searchTitleCondition(String searchTitle) {

        if (StringUtils.hasText(searchTitle)) return null;

        return gathering.title.contains(searchTitle);
    }


    private BooleanExpression radiusDistanceCondition(GeoRadiusDto geoRadius) {


        if (geoRadius != null) {
            CoordinatesDto coordinates = geoRadius.getCoordinates();


            // 좌표를 Expression<Double>로 변환
            Expression<Double> latitude = create(coordinates.getY()); // 경도(longitude)는 x 좌표
            Expression<Double> longitude = create(coordinates.getX()); // 위도(latitude)는 y 좌표


            // 지구 반지름: km 단위로 설정 (6371km)
            final double EARTH_RADIUS_KM = 6371.0;

            QCoordinates coordinate = gathering.location.coordinates;

            return acos(
                    sin(radians(latitude))
                            .multiply(sin(radians(coordinate.y)))
                            .add(
                                    cos(radians(latitude))
                                            .multiply(cos(radians(coordinate.y)))
                                            .multiply(cos(radians(coordinate.x)
                                                    .subtract(radians(longitude))))
                            )
            ).multiply(EARTH_RADIUS_KM).loe(geoRadius.getRadiusDistance());
        }

        return null;
    }

    private BooleanExpression goalDistanceCondition(GoalDistance goalDistance) {

        if (goalDistance != null) {
            return gathering.goalDistance.eq(goalDistance);
        }
        return null;
    }

    private BooleanExpression runningConceptCondition(RunningConcept concept) {

        if (concept != null) {
            return gathering.concept.eq(concept);
        }
        return null;
    }


    // PathBuilder 동적 표현식 활용 (이유는 pr에 작성)
    private OrderSpecifier<?> orderCondition(GatheringOrderField gatheringOrderField,
                                             Direction direction) {

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
