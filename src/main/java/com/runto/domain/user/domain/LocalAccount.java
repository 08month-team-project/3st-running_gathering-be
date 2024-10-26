package com.runto.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="local_id")
    private Long id;

    private String password;
}
