package com.api.documentApp.domain.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
}
