package com.api.documentApp.service.user.impl;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentEntityDTOMapper;
import com.api.documentApp.domain.mapper.user.UpdateUserMapper;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import com.api.documentApp.domain.mapper.usergroup.UserGroupResponseMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserResponseMapper userResponseMapper;
    private final UpdateUserMapper updateUserMapper;
    private final DocumentEntityDTOMapper documentEntityDTOMapper;
    private final DocumentRepo documentRepo;
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
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        UserGroupEntity userGroup = user.getUserGroup();
        return UserGroupResponseDTO.builder()
                .name(userGroup.getName())
                .members(userResponseMapper.toDto(userGroup.getUsers()))
                .build();
    }

    @Override
    public UserResponseDTO updateUserById(Long userId, UserRequestDTO requestDTO) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        user = updateUserMapper.mapUserEntityWithRequestDTO(user, requestDTO);
        return userResponseMapper.toDto(userRepo.save(user));
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        userRepo.delete(user);
    }

    @Override
    public List<DocumentResponseDTO> getUserDocuments(Long userId, String userNameFromAccess) throws UserNotFoundByIdException, NotEnoughRightsException {
        var reqUser = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );
        var user = userRepo.findByEmail(userNameFromAccess).get();

        var isGroupMember = reqUser.getUserGroup()!=null && reqUser.getUserGroup().getUsers().contains(user);
        if (
                user == reqUser
                || user.getRole() == Role.ADMIN
                || isGroupMember
        ) {

            return documentEntityDTOMapper.toDto(documentRepo.findAllByUser(reqUser));
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }

    }
}
