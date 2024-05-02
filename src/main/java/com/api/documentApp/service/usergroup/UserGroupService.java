package com.api.documentApp.service.usergroup;

import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserGroupService {
    public UserGroupResponseDTO createUserGroup(UserGroupRequestDTO userGroupRequestDTO) throws UsernameNotFoundException;
    public UserGroupResponseDTO getUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException;
    public UserGroupResponseDTO updateUserGroupById(Long userGroupId, UserGroupRequestDTO userGroupRequestDTO) throws UserGroupNotFoundByIdException;
    public void deleteUserGroupById(Long userGroupId) throws UserGroupNotFoundByIdException;
    public List<UserGroupResponseDTO> getAllUserGroups();
}
