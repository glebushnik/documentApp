package com.api.documentApp.domain.mapper.user;

import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper extends EntityResponseMapper<UserResponseDTO, UserEntity> {
    @Override
    UserEntity toEntity(UserResponseDTO dto);

    @Override
    UserResponseDTO toDto(UserEntity entity);

    @Override
    List<UserEntity> toEntity(List<UserResponseDTO> dtoList);

    @Override
    List<UserResponseDTO> toDto(List<UserEntity> entityList);
}
