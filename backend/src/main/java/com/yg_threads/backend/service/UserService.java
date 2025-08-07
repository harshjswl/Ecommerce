package com.yg_threads.backend.service;

import com.yg_threads.backend.dto.JwtResponseDTO;
import com.yg_threads.backend.dto.LoginRequestDTO;
import com.yg_threads.backend.dto.RegisterRequestDTO;

public interface UserService {
    String registerUser(RegisterRequestDTO requestDTO);
    JwtResponseDTO verifyOtp(String email, String otp);
    String resendOtp(String email);
    // NEW: Add login method
    JwtResponseDTO login(LoginRequestDTO loginRequestDTO);
}