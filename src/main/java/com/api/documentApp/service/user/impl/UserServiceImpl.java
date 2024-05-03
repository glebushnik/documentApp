package com.api.documentApp.service.user.impl;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import com.api.documentApp.domain.mapper.usergroup.UserGroupResponseMapper;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
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
    private final UserGroupResponseMapper userGroupResponseMapper;
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userResponseMapper.toDto(userRepo.findAll());
    }

    @Override
    public UserResponseDTO getUserById(Long userId) throws UserNotFoundByIdException {
        return userResponseMapper.toDto(userRepo.findById(userId).orElseThrow(()
                ->new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден",userId))));
    }



    @Override
    public UserGroupResponseDTO getUserGroup(Long userId) throws UserNotFoundByIdException {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователя с таким id : %d не существует", userId)));
        UserGroupEntity userGroup = user.getUserGroup();
        return UserGroupResponseDTO.builder()
                .name(userGroup.getName())
                .members(userResponseMapper.toDto(userGroup.getUsers()))
                .build();
    }

    @Override
    public UserResponseDTO updateUserById(Long userId) throws UserNotFoundByIdException {
        return null;
    }
}
