package com.api.documentApp.domain.mapper.usergroup;

import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserGroupRequestMapper extends EntityRequestMapper<UserGroupRequestDTO, UserGroupEntity> {
    @Override
    UserGroupEntity toEntity(UserGroupRequestDTO dto);

    @Override
    UserGroupRequestDTO toDto(UserGroupEntity entity);

    @Override
    List<UserGroupEntity> toEntity(List<UserGroupRequestDTO> dtoList);

    @Override
    List<UserGroupRequestDTO> toDto(List<UserGroupEntity> entityList);
}
