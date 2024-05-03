package com.api.documentApp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.ZonedDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Instant expiration;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
