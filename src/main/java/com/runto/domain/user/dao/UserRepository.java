package com.runto.domain.user.dao;


import com.runto.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query("SELECT u " +
            " FROM User u " +
            " JOIN FETCH u.localAccount " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
