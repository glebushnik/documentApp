package com.api.documentApp.service.user.impl;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserResponseMapper userResponseMapper;
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userResponseMapper.toDto(userRepo.findAll());
    }
}
