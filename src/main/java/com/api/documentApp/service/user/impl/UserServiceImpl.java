package com.api.documentApp.service.user.impl;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentEntityDTOMapper;
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.domain.mapper.user.UpdateUserMapper;
import com.api.documentApp.domain.mapper.user.UserResponseMapper;
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
    public UserResponseDTO updateUserById(Long userId, UserRequestDTO requestDTO) throws UserNotFoundByIdException, UserGroupNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        user = updateUserMapper.mapUserEntityWithRequestDTO(user, requestDTO);
        if(requestDTO.getGroupId()!=null) {
            var userGroup = userGroupRepo.findById(requestDTO.getGroupId()).orElseThrow(
                    () -> new UserGroupNotFoundByIdException(
                            String.format("Группа пользователей с id : %d не найдена", requestDTO.getGroupId())
                    )
            );
            var prevUserGroup = user.getUserGroup() != null ? user.getUserGroup() : null;
            if (prevUserGroup != null) {
                var prevUserGroupMembers = prevUserGroup.getUsers();
                prevUserGroupMembers.remove(user);
                userGroupRepo.save(prevUserGroup);
            }
            user.setUserGroup(userGroup);
            var newUserGroupMembers = userGroup.getUsers();
            newUserGroupMembers.add(user);
            userGroup.setUsers(newUserGroupMembers);
            userGroupRepo.save(userGroup);
        }
        return userResponseMapper.toDto(userRepo.save(user));
    }

    @Override
    public void deleteUserById(Long userId) throws UserNotFoundByIdException {
        var user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден", userId)));
        var refresh = user.getRefreshToken();
        refreshTokenRepo.delete(refresh);
        var userGroup = user.getUserGroup();
        if(userGroup!=null) {
            var groupMembers = userGroup.getUsers();
            groupMembers.remove(user);
            userGroup.setUsers(groupMembers);
            userGroupRepo.save(userGroup);
        }
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

            return documentResponseMapper.toDto(documentRepo.findAllByUser(reqUser));
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }

    @Override
    public UserResponseDTO getCurrentUser(String userNameFromAccess) {
        return userResponseMapper.toDto(userRepo.findByEmail(userNameFromAccess).get());
    }
}
