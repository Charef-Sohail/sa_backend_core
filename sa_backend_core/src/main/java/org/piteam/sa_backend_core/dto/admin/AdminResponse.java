package org.piteam.sa_backend_core.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.piteam.sa_backend_core.models.Role;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AdminResponse {
    private Instant createdAt;
    private String id;
    private String name;
    private String email;
    private Role role;
}