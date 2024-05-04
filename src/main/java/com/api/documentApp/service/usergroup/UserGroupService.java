package com.api.documentApp.service.usergroup;

import com.api.documentApp.domain.DTO.user.UserEmailRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.exception.user.UserNotFoundByEmailException;
import com.api.documentApp.exception.usergroup.UserGroupContainsNoSuchUsersException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserGroupService {
    public UserGroupResponseDTO createUserGroup(UserGroupRequestDTO userGroupRequestDTO) throws UsernameNotFoundException;
    public UserGroupResponseDTO getUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException;
    public UserGroupResponseDTO updateUserGroupById(Long userGroupId, UserGroupRequestDTO userGroupRequestDTO) throws UserGroupNotFoundByIdException;
    public void deleteUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException;
    public List<UserGroupResponseDTO> getAllUserGroups();
    public UserGroupResponseDTO addUserToGroup(Long userGroupId, UserEmailRequestDTO userEmailRequestDTO) throws UserNotFoundByEmailException,
            UserGroupNotFoundByIdException;
    public UserGroupResponseDTO deleteUserFromGroup(Long userGroupId, UserEmailRequestDTO userEmailRequestDTO)
            throws UserNotFoundByEmailException,
            UserGroupNotFoundByIdException,
            UserGroupContainsNoSuchUsersException;
}
