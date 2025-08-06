package com.yg_threads.backend.controller;

import com.yg_threads.backend.dto.JwtResponseDTO;
import com.yg_threads.backend.dto.RegisterRequestDTO;
import com.yg_threads.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return ResponseEntity.ok(userService.registerUser(requestDTO));
    }

    // You can add /login and /verify-email endpoints here later
}
