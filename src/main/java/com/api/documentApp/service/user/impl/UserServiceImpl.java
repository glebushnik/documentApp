package com.api.documentApp.service.user.impl;

import com.api.documentApp.config.entity.UserEntity;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }
}
