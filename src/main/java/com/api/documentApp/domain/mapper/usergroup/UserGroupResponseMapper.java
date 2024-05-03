package com.api.documentApp.domain.mapper.usergroup;

import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserGroupResponseMapper extends EntityResponseMapper<UserGroupResponseDTO, UserGroupEntity> {
    @Override
    UserGroupEntity toEntity(UserGroupResponseDTO dto);

    @Override
    @Mapping(target = "id", source = "entity.id")
    UserGroupResponseDTO toDto(UserGroupEntity entity);

    @Override
    List<UserGroupEntity> toEntity(List<UserGroupResponseDTO> dtoList);

    @Override
    @Mapping(target = "id", source = "entity.id")
    List<UserGroupResponseDTO> toDto(List<UserGroupEntity> entityList);
}
