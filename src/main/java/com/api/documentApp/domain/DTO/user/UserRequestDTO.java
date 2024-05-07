package com.api.documentApp.domain.DTO.user;

import com.api.documentApp.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String post;
    private String department;
    private Role role;
    private Long groupId;
}
