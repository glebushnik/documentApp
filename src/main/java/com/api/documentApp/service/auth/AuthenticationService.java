package com.api.documentApp.service.auth;

import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.DTO.auth.AuthenticationRequest;
import com.api.documentApp.domain.DTO.auth.RegisterRequest;
import com.api.documentApp.domain.DTO.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {

        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .department(request.getDepartment())
                .patronymic(request.getPatronymic())
                .post(request.getPost())
                .isActive(true)
                .build();
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user.getEmail());
        return  AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user.getEmail());
        return  AuthenticationResponse.builder().token(jwtToken).build();
    }


}
