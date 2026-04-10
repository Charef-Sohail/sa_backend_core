package org.piteam.sa_backend_core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.piteam.sa_backend_core.models.Role;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String id;
    private String name;
    private String email;
    private Role role;
}
