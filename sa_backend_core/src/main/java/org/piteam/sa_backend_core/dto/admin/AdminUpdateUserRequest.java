package org.piteam.sa_backend_core.dto.admin;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.piteam.sa_backend_core.models.Role;

@Data
public class AdminUpdateUserRequest {
    private String name;
    @Email(message = "Invalid email")
    private String email;
    private Role role;
}