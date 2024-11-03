package com.runto.domain.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="o_auth2_id")
    private Long id;

    @Column(name="o_auth2_key")
    private String oAuth2Key;
}
