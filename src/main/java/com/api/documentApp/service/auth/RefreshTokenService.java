package com.api.documentApp.service.auth;

import com.api.documentApp.domain.entity.RefreshToken;
import com.api.documentApp.exception.refresh.RefreshTokenNotFoundByToken;
import com.api.documentApp.exception.user.UserNotFoundByEmailException;
import com.api.documentApp.repo.token.RefreshTokenRepo;
import com.api.documentApp.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
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

    public RefreshToken updateRefreshToken(String token) throws RefreshTokenNotFoundByToken {
        var refreshToken = refreshTokenRepo.findRefreshTokenByToken(token).orElseThrow(()
                -> new RefreshTokenNotFoundByToken(String.format("RefreshToken с token : %s не найден.", token)));
        refreshTokenRepo.delete(refreshToken);

        return createRefreshToken(refreshToken.getUser().getEmail());
    }
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findRefreshTokenByToken(token);
    }

}