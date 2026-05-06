package org.piteam.sa_backend_core.dto.admin;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.piteam.sa_backend_core.models.Role;

import java.time.LocalDate;

@Data
public class AdminUpdateUserRequest {
    private String name;
    @Email(message = "Invalid email")
    private String email;
    private Role role;
    private LocalDate birthDate;
    private String university;
}