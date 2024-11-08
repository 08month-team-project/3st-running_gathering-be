package com.runto.domain.coupon.dao;


import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.domain.UserCoupons;
import com.runto.domain.coupon.type.CouponStatus;
import com.runto.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponsRepository extends JpaRepository<UserCoupons, Long> {

    Page<UserCoupons> findByStatus(CouponStatus status, Pageable pageable);

    boolean existsByUserAndCoupon(User user, Coupon coupon);
}
