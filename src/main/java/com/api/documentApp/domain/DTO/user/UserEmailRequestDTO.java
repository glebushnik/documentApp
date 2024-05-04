package com.api.documentApp.domain.DTO.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailRequestDTO {
    @NotBlank(message = "Email пользователя не может быть пустым")
    private String userEmail;
}
