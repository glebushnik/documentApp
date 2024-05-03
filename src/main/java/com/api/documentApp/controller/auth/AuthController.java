package com.api.documentApp.controller.auth;

import com.api.documentApp.domain.DTO.auth.*;
import com.api.documentApp.domain.entity.RefreshToken;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.mapper.auth.AuthRequestMapper;
import com.api.documentApp.exception.refresh.RefreshTokenNotFoundByToken;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.auth.AuthenticationService;
import com.api.documentApp.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            String accessToken = authenticationService.register(request).getToken();
            String refreshToken = refreshTokenService.createRefreshToken(request.getEmail()).getToken();
            System.out.println(accessToken);
            System.out.println(refreshToken);
            return ResponseEntity.ok(
                    JWTResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());
        return ResponseEntity.ok(
                JWTResponse.builder()
                .accessToken(authenticationService.authenticate(request).getToken())
                .refreshToken(refreshToken.getToken())
                .build()
        );
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getToken()).get();
            String userName = refreshToken.getUser().getEmail();
            return ResponseEntity.ok().body(
                    JWTResponse.builder()
                            .refreshToken(refreshTokenService.updateRefreshToken(refreshToken.getToken()).getToken())
                            .accessToken(jwtService.generateToken(userName))
                                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
