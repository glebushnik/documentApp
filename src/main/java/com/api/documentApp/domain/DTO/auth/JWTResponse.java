package com.api.documentApp.domain.DTO.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
}
