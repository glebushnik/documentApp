package com.api.documentApp.controller.auth;

import com.api.documentApp.domain.DTO.auth.*;
import com.api.documentApp.domain.entity.RefreshToken;
import com.api.documentApp.domain.mapper.auth.AuthRequestMapper;
import com.api.documentApp.service.auth.AuthenticationService;
import com.api.documentApp.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final AuthRequestMapper authRequestMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());
        return ResponseEntity.ok(
                JWTResponse.builder()
                .accessToken(authenticationService.authenticate(request))
                .refreshToken(refreshToken.getToken())
                .build()
        );
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(refreshTokenService.findByToken(request.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String accessToken = authenticationService.authenticate(
                                authRequestMapper.toDto(user)
                        );
                        return JWTResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(request.getToken())
                                .build();
                    }).orElseThrow(() -> new RuntimeException("Wrong refresh token"))
            );
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
