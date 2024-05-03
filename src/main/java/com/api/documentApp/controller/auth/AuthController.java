package com.api.documentApp.controller.auth;

import com.api.documentApp.domain.DTO.auth.AuthenticationRequest;
import com.api.documentApp.domain.DTO.auth.JWTResponse;
import com.api.documentApp.domain.DTO.auth.RegisterRequest;
import com.api.documentApp.domain.DTO.auth.AuthenticationResponse;
import com.api.documentApp.domain.entity.RefreshToken;
import com.api.documentApp.service.auth.AuthenticationService;
import com.api.documentApp.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
