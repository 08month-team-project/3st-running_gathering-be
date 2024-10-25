package com.runto.domain.user.dao;

import com.runto.domain.user.domain.LocalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalAccountRepository extends JpaRepository<LocalAccount, Long> {
    Optional<LocalAccount> findByUserId(Long userId);;
}
