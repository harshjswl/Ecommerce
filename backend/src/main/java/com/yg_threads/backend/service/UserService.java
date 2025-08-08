package com.yg_threads.backend.service;

import com.yg_threads.backend.dto.*;

public interface UserService {
    String registerUser(RegisterRequestDTO requestDTO);
    JwtResponseDTO verifyOtp(String email, String otp);
    String resendOtp(String email);
    JwtResponseDTO login(LoginRequestDTO loginRequestDTO);
    String forgotPassword(ForgotPasswordRequestDTO requestDTO);
    String resetPassword(ResetPasswordRequestDTO requestDTO);
}