package org.piteam.sa_backend_core.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password")
    private String password;

    @Min(value = 17, message = "Age must be at least 17")
    @Max(value = 100, message = "Invalid age")
    private Integer age;

    private String university;
}
