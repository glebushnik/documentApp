package com.api.documentApp.domain.mapper.user;

import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserMapper {
    private final PasswordEncoder passwordEncoder;
    public UserEntity mapUserEntityWithRequestDTO(UserEntity user, UserRequestDTO requestDTO) {
        user.setFirstName(requestDTO.getFirstName() != null ? requestDTO.getFirstName() : user.getFirstName());
        user.setLastName(requestDTO.getLastName() != null ? requestDTO.getLastName() : user.getLastName());
        user.setPatronymic(requestDTO.getPatronymic() != null ? requestDTO.getPatronymic() : user.getPatronymic());
        user.setEmail(requestDTO.getEmail() != null ? requestDTO.getEmail() : user.getEmail());
        user.setPost(requestDTO.getPost() != null ? requestDTO.getPost() : user.getPost());
        user.setRole(requestDTO.getRole() != null ? requestDTO.getRole() : user.getRole());
        user.setDepartment(requestDTO.getDepartment() != null ? requestDTO.getDepartment() : user.getDepartment());
        user.setPassword(requestDTO.getPassword() != null ? passwordEncoder.encode(requestDTO.getPassword()) : user.getPassword());
        return user;
    }
}
