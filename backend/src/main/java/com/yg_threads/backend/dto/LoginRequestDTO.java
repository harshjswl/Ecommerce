package com.yg_threads.backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email or phone number is required")
    private String emailOrNumber;
    @NotBlank(message="Password is required")
    private  String password;
}
