package com.runto.domain.user.dao;

import com.runto.domain.user.domain.OAuth2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2Repository extends JpaRepository<OAuth2, Long> {
    Optional<OAuth2> findByOAuth2Key(String oauth2Key);
}
