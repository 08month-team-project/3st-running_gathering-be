package com.runto.domain.user.dao;


import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    @Query("SELECT u " +
            " FROM User u " +
            " JOIN FETCH u.localAccount " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u " +
            " FROM User u " +
            " JOIN FETCH u.localAccount " +
            "WHERE u.nickname = :nickname")
    Optional<User> findByNickname(String nickname);


    @Query("SELECT u " +
            " FROM User u " +
            " JOIN FETCH u.oAuth2 s " +
            "WHERE s.oAuth2Key = :oauth2_key")
    Optional<User> findByOAuth2OAuth2Key(@Param("oauth2_key") String oauth2_key);

    boolean existsByNickname(String nickname);

    List<User> findByModifiedAtBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus status);

}
