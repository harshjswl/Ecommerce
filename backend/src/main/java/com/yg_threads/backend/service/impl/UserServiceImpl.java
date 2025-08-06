package com.yg_threads.backend.service.impl;

import com.yg_threads.backend.dto.JwtResponseDTO;
import com.yg_threads.backend.dto.RegisterRequestDTO;
import com.yg_threads.backend.entity.Role;
import com.yg_threads.backend.entity.User;
import com.yg_threads.backend.exception.UserException;
import com.yg_threads.backend.repository.UserRepository;
import com.yg_threads.backend.security.JwtUtile;
import com.yg_threads.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtile jwtUtile;

    @Override
    @Transactional
    public JwtResponseDTO registerUser(RegisterRequestDTO requestDTO){
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new UserException("Email is already in use.");
        }
        if (userRepository.existsByNumber(requestDTO.getNumber())) {
            throw new UserException("Number is already in use.");
        }
        User newUser = User.builder()
                .name(requestDTO.getName())
                .number(requestDTO.getNumber())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .enabled(false)
                .emailVerified(false)
                .accountNonLocked(true)
                .roles(Set.of(Role.ROLE_USER))
                .build();
        userRepository.save(newUser);
        String token = jwtUtile.generateToken(newUser.getEmail());
        return JwtResponseDTO.builder()
                .token(token)
                .message("User registered successfully. Please verify your email.")
                .build();
    }
}
