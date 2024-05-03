package com.api.documentApp.domain.mapper.user;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper extends EntityResponseMapper<UserResponseDTO, UserEntity> {
    @Override
    UserEntity toEntity(UserResponseDTO dto);

    @Override
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "isActive", source = "entity.active")
    UserResponseDTO toDto(UserEntity entity);

    @Override
    List<UserEntity> toEntity(List<UserResponseDTO> dtoList);

    @Override
    List<UserResponseDTO> toDto(List<UserEntity> entityList);
}
