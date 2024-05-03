package com.api.documentApp.service.auth;

import com.api.documentApp.domain.entity.RefreshToken;
import com.api.documentApp.exception.user.UserNotFoundByEmailException;
import com.api.documentApp.repo.token.RefreshTokenRepo;
import com.api.documentApp.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshToken createRefreshToken(String username) {
        return refreshTokenRepo.save(
                RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(Instant.now().plusSeconds(1296000))
                .user(userRepo.findByEmail(username).get())
                .build()
        );
    }
}
