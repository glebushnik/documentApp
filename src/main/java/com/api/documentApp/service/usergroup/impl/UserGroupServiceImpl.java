package com.api.documentApp.service.usergroup.impl;

import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import com.api.documentApp.domain.mapper.usergroup.UserGroupResponseMapper;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import com.api.documentApp.service.usergroup.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepo userGroupRepo;
    private final UserRepo userRepo;
    private final UserGroupResponseMapper userGroupResponseMapper;
    private final UserResponseMapper userResponseMapper;
    @Override
    public UserGroupResponseDTO createUserGroup(UserGroupRequestDTO userGroupRequestDTO) throws UsernameNotFoundException {
        List<String>emails = userGroupRequestDTO.getEmails();
        List<UserEntity> users = emails.stream()
                .map(email -> userRepo.findByEmail(email).orElseThrow(()
                        -> new UsernameNotFoundException(String.format("Пользователя с таким email : %s не существует", email))))
                .collect(Collectors.toList());
        var userGroup = UserGroupEntity.builder()
                .name(userGroupRequestDTO.getName())
                .users(users)
                .build();
        userGroupRepo.save(userGroup);
        users.forEach(userEntity -> userEntity.setUserGroup(userGroup));
        userRepo.saveAll(users);
        return UserGroupResponseDTO.builder()
                .name(userGroup.getName())
                .members(userResponseMapper.toDto(users))
                .build();

    }

    @Override
    public UserGroupResponseDTO getUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException {
        var userGroup = userGroupRepo.findById(userGroupId).orElseThrow(()
                -> new UserGroupNotFoundByIdException(String.format("Группы пользователей с id : %d не найдено.", userGroupId)));
        return UserGroupResponseDTO.builder()
                .name(userGroup.getName())
                .members(userResponseMapper.toDto(userGroup.getUsers()))
                .build();
    }

    //todo после добавления documents сделать апдейт
    @Override
    public UserGroupResponseDTO updateUserGroupById(Long userGroupId, UserGroupRequestDTO userGroupRequestDTO) throws UserGroupNotFoundByIdException {
        var userGroup = userGroupRepo.findById(userGroupId).orElseThrow(()
                -> new UserGroupNotFoundByIdException(String.format("Группы пользователей с id : %d не найдено.", userGroupId)));
        return null;
    }

    @Override
    public void deleteUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException {
        var userGroup = userGroupRepo.findById(userGroupId).orElseThrow(()
                -> new UserGroupNotFoundByIdException(String.format("Группы пользователей с id : %d не найдено.", userGroupId)));
        userGroupRepo.deleteById(userGroupId);
    }

    @Override
    public List<UserGroupResponseDTO> getAllUserGroups() {
        List<UserGroupEntity> userGroups = userGroupRepo.findAll();
        List<UserGroupResponseDTO> userGroupResponseDTOS = userGroups.stream()
                .map(userGroup -> UserGroupResponseDTO.builder()
                        .name(userGroup.getName())
                        .id(userGroup.getId())
                        .members(userGroup.getUsers().stream()
                                .map(userResponseMapper::toDto)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return userGroupResponseDTOS;

    }
}
