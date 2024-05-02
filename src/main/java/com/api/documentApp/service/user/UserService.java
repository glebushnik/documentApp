package com.api.documentApp.service.user;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;

import java.util.List;

public interface UserService {
    public List<UserResponseDTO> getAllUsers();
    //public UserGroupResponseDTO getUserGroup(Long userId);
}
