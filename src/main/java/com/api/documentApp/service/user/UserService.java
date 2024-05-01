package com.api.documentApp.service.user;

import com.api.documentApp.config.entity.UserEntity;

import java.util.List;

public interface UserService {
    public List<UserEntity> getAllUsers();
}
