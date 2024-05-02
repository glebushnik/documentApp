package com.api.documentApp.service.usergroup;

import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserGroupService {
    public UserGroupResponseDTO createUserGroup(UserGroupRequestDTO userGroupRequestDTO) throws UsernameNotFoundException;
}
