package com.api.documentApp.domain.DTO.user;

import com.api.documentApp.domain.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String post;
    private Role role;
    private String isActive;
}
