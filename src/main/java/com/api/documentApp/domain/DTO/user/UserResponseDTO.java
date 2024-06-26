package com.api.documentApp.domain.DTO.user;

import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseNoMembersDTO;
import com.api.documentApp.domain.enums.Role;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private String post;
    private String department;
    private Role role;
    private List<UserGroupResponseNoMembersDTO> groupResponseDTOs;
    private boolean isActive;
}
