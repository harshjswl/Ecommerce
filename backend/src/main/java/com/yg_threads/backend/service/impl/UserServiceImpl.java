package com.yg_threads.backend.service.impl;

import com.yg_threads.backend.dto.JwtResponseDTO;
import com.yg_threads.backend.dto.LoginRequestDTO;
import com.yg_threads.backend.dto.RegisterRequestDTO;
import com.yg_threads.backend.entity.Role;
import com.yg_threads.backend.entity.User;
import com.yg_threads.backend.exception.UserException;
import com.yg_threads.backend.repository.UserRepository;
import com.yg_threads.backend.security.JwtUtile;
import com.yg_threads.backend.service.EmailService;
import com.yg_threads.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom; // ENHANCEMENT: Use SecureRandom
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtile jwtUtile;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    // ENHANCEMENT: Re-use SecureRandom instance
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public String registerUser(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new UserException("Email is already in use.");
        }
        if (userRepository.existsByNumber(requestDTO.getNumber())) {
            throw new UserException("Number is already in use.");
        }

        String otp = generateOtp();

        User newUser = User.builder()
                .name(requestDTO.getName())
                .number(requestDTO.getNumber())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .roles(Set.of(Role.ROLE_USER))
                .otp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(10))
                .enabled(false)
                .emailVerified(false) // Explicitly set for clarity
                .accountNonLocked(true) // Explicitly set for clarity
                .build();

        userRepository.save(newUser);
        emailService.sendOtpEmail(newUser.getEmail(), otp);

        return "User registered successfully. Please check your email for the OTP.";
    }

    @Override
    @Transactional
    public JwtResponseDTO verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with this email."));

        if (user.isEnabled()) {
            throw new UserException("Account is already verified.");
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new UserException("Invalid OTP.");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new UserException("OTP has expired. Please request a new one.");
        }

        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        String token = jwtUtile.generateToken(user.getEmail());

        return JwtResponseDTO.builder()
                .token(token)
                .message("Email verified successfully. Account activated.")
                .build();
    }

    @Override
    @Transactional
    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found with this email."));

        if (user.isEnabled()) {
            throw new UserException("Account is already verified.");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return "A new OTP has been sent to your email.";
    }

    private String generateOtp() {
        // ENHANCEMENT: Use SecureRandom for cryptographically strong random numbers
        int otpValue = 100000 + this.secureRandom.nextInt(900000);
        return String.valueOf(otpValue);
    }

    @Override
    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmailOrNumber(),
                        loginRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Find user to check if they are enabled
        User user = userRepository.findByEmail(authentication.getName())
                .or(() -> userRepository.findByNumber(authentication.getName()))
                .orElseThrow(() -> new UserException("Error fetching user details after authentication."));

        if (!user.isEnabled()) {
            throw new UserException("User account is not yet verified. Please check your email for OTP.");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtile.generateToken(authentication.getName());

        return JwtResponseDTO.builder()
                .token(token)
                .message("User logged in successfully.")
                .build();
    }
}