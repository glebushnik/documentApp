package com.api.documentApp.service.user;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    public List<UserResponseDTO> getAllUsers();
}
