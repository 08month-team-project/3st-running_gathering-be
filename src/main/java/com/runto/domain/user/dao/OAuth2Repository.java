package com.runto.domain.user.dao;

import com.runto.domain.user.domain.OAuth2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2Repository extends JpaRepository<OAuth2, Long> {
    @Query("SELECT o " +
            "FROM OAuth2 o " +
            "WHERE o.oAuth2Key = :oAuth2Key")
    Optional<OAuth2> findByOAuth2Key(@Param("oAuth2Key") String oAuth2Key);
}
