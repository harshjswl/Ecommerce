package com.yg_threads.backend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Number is required")
    private String number;
    @NotBlank(message="Email is required")
    @Email( message = "Invalid email format")
    private String email;
    @NotBlank(message = "Invalid email formate")
    @Size(min = 6,message = "Password must be at least 6 character")
    private String password;
}
