package com.api.documentApp.domain.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "dsadsadsa")
    private String firstName;
    private String lastName;
    private String patronymic;
    private String department;
    private String post;
    private String email;
    private String password;
}
