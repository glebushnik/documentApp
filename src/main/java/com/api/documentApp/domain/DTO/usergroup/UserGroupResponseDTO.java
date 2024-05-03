package com.api.documentApp.domain.DTO.usergroup;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupResponseDTO {
    private Long id;
    private String name;
    private List<UserResponseDTO> members;
}
