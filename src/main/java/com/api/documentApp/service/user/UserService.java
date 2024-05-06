package com.api.documentApp.service.user;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;

import java.util.List;

public interface UserService {
    public List<UserResponseDTO> getAllUsers();
    public UserResponseDTO getUserById(Long userId) throws UserNotFoundByIdException;
    public UserResponseDTO updateUserById(Long userId, UserRequestDTO requestDTO) throws UserNotFoundByIdException, UserGroupNotFoundByIdException;
    public void deleteUserById(Long userId) throws UserNotFoundByIdException;
    public UserGroupResponseDTO getUserGroup(Long userId) throws UserNotFoundByIdException;
    public List<DocumentResponseDTO> getUserDocuments(Long userId,String userNameFromAccess)
            throws UserNotFoundByIdException, NotEnoughRightsException;
}
