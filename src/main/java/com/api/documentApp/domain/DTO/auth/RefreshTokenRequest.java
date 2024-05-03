package com.api.documentApp.domain.DTO.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String token;
}
