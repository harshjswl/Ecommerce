package com.yg_threads.backend.controller;

import com.yg_threads.backend.dto.*;
import com.yg_threads.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        String message = userService.registerUser(requestDTO);
        return ResponseEntity.ok(Map.of("message", message));
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(userService.login(loginRequestDTO));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponseDTO> verifyOtp(@Valid @RequestBody OtpVerificationRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.verifyOtp(requestDTO.getEmail(), requestDTO.getOtp()));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, String>> resendOtp(@Valid @RequestBody OtpRequestDTO requestDTO) {
        String message = userService.resendOtp(requestDTO.getEmail());
        return ResponseEntity.ok(Map.of("message", message));
    }
    @PostMapping("/forgot-password")
    // MODIFIED: Use the new ForgotPasswordRequestDTO
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        String message = userService.forgotPassword(requestDTO);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO requestDTO) {
        String message = userService.resetPassword(requestDTO);
        return ResponseEntity.ok(Map.of("message", message));
    }

    // You can add /login endpoint here later
}