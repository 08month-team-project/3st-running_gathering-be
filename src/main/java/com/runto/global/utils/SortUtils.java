package com.runto.global.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

public class SortUtils {

    /**
     *
     * @param entityClass: 엔티티타입
     * @param fieldName: 정렬 기준이 될 필드이름
     * @param direction: 정렬 방식
     * @return OrderSpecifier<?>
     */
    public static <T> OrderSpecifier<?> getOrderSpecifier(Class<T> entityClass,
                                                          String fieldName,
                                                          Sort.Direction direction) {
        // 엔티티의 PathBuilder 생성
        PathBuilder<T> entityPath = new PathBuilder<>(entityClass, entityClass.getSimpleName().toLowerCase());

        // 정렬 순서 지정
        Order order = direction.isAscending() ? Order.ASC : Order.DESC;

        // 필드 이름과 정렬 순서를 사용하여 OrderSpecifier 생성
        return new OrderSpecifier(order, entityPath.get(fieldName));
    }


    // 구현했던 여러 버전 중에서 사용했던 메서드 중 하나인데, 나중에 혹시 쓸 수도 있으니 일단 남겨둡니다.
//    public static Sort.Direction getFirstDirectionFromPageable(Pageable pageable) {
//        return pageable.getSort()
//                .stream()
//                .findFirst()                 // 첫 번째 정렬 조건 가져오기
//                .map(Sort.Order::getDirection) // Direction 값 추출
//                .orElse(Sort.Direction.DESC);  // 기본값 설정 (필요 시 변경 가능)
//    }
//    public static List<Sort.Direction> getAllDirectionFromPageable(Pageable pageable) {
//        return pageable.getSort()
//                .stream()// 첫 번째 정렬 조건 가져오기
//                .map(order -> order.getDirection() == null ? Sort.Direction.DESC : order.getDirection()) // Direction 값 추출
//                .toList();  // 기본값 설정 (필요 시 변경 가능)
//    }
}
