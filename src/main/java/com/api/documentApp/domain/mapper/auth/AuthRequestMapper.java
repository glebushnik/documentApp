package com.api.documentApp.domain.mapper.auth;

import com.api.documentApp.domain.DTO.auth.AuthenticationRequest;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.mapper.EntityRequestMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthRequestMapper extends EntityRequestMapper<AuthenticationRequest, UserEntity> {
    @Override
    UserEntity toEntity(AuthenticationRequest dto);

    @Override
    AuthenticationRequest toDto(UserEntity entity);

    @Override
    List<UserEntity> toEntity(List<AuthenticationRequest> dtoList);

    @Override
    List<AuthenticationRequest> toDto(List<UserEntity> entityList);
}
