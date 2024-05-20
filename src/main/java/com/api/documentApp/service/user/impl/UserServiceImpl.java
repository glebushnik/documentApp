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
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.domain.mapper.user.UpdateUserMapper;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import com.api.documentApp.domain.mapper.usergroup.UserGroupResponseMapper;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.token.RefreshTokenRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import com.api.documentApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserGroupRepo userGroupRepo;
    private final UserResponseMapper userResponseMapper;
    private final UpdateUserMapper updateUserMapper;
    private final DocumentResponseMapper documentResponseMapper;
    private final DocumentRepo documentRepo;
    private final RefreshTokenRepo refreshTokenRepo;
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
    public List<UserGroupResponseDTO> getUserGroups(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        var userGroups = user.getUserGroups();
        return userGroupResponseMapper.toDto(userGroups);
    }

    @Override
    public UserResponseDTO updateUserById(Long userId, UserRequestDTO requestDTO) throws UserNotFoundByIdException, UserGroupNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId))
        );

        updateUserMapper.mapUserEntityWithRequestDTO(user, requestDTO);
        userGroupRepo.saveAll(user.getUserGroups());
        return userResponseMapper.toDto(userRepo.save(user));
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        var refresh = user.getRefreshToken();
        refreshTokenRepo.delete(refresh);
        var userGroups = user.getUserGroups();
        userGroups.forEach(userGroup -> {
            var users = userGroup.getUsers();
            users.remove(user);
            userGroup.setUsers(users);
        });
        userGroupRepo.saveAll(userGroups);
        userRepo.delete(user);
    }

    @Override
    public List<DocumentResponseDTO> getUserDocuments(Long userId, String userNameFromAccess) throws UserNotFoundByIdException, NotEnoughRightsException {
        var reqUser = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );
        var user = userRepo.findByEmail(userNameFromAccess).get();
        var userGroups = user.getUserGroups();
        userGroups.retainAll(reqUser.getUserGroups());
        if(!userGroups.isEmpty()){
            return documentResponseMapper.toDto(documentRepo.findAllByUser(reqUser));
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }

    @Override
    public UserResponseDTO getCurrentUser(String userNameFromAccess) {
        return userResponseMapper.toDto(userRepo.findByEmail(userNameFromAccess).get());
    }

    @Override
    public List<UserResponseDTO> getCurrentUserGroupMembers(String userNameFromAccess) {
        var user = userRepo.findByEmail(userNameFromAccess).get();

        var userGroups = user.getUserGroups();

        List<UserEntity> users = userGroups.stream()
                .flatMap(userGroup -> userGroup.getUsers().stream())
                .collect(Collectors.toSet()).stream().toList();
        return userResponseMapper.toDto(users);
    }

    @Override
    public UserResponseDTO disableUser(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );


        user.setActive(false);
        return userResponseMapper.toDto(userRepo.save(user));
    }

    @Override
    public UserResponseDTO activateUser(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );


        user.setActive(true);
        return userResponseMapper.toDto(userRepo.save(user));
    }
}


