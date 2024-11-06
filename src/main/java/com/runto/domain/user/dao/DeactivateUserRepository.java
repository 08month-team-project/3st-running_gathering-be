package com.runto.domain.user.dao;

import com.runto.domain.user.domain.DeactivateUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeactivateUserRepository extends JpaRepository<DeactivateUser, Long> {
}
