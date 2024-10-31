package com.runto.domain.coupon.dao;


import com.runto.domain.coupon.domain.Coupon;
import com.runto.domain.coupon.domain.UserCoupons;
import com.runto.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponsRepository extends JpaRepository<UserCoupons, Long> {


    boolean existsByUserAndCoupon(User user, Coupon coupon);
}
