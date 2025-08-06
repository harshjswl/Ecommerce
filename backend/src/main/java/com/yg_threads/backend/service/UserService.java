package com.yg_threads.backend.service;

import com.yg_threads.backend.dto.JwtResponseDTO;
import com.yg_threads.backend.dto.RegisterRequestDTO;

public interface UserService {
    public JwtResponseDTO registerUser(RegisterRequestDTO requestDTO);
}
