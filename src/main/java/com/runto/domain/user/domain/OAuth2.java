package com.runto.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="oauth2_id")
    private Long id;

    @Column(name="oauth2_key")
    private String oAuth2Key;
}
