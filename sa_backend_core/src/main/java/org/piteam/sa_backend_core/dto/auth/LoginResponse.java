package org.piteam.sa_backend_core.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private String token;
    private String email;
    private String role;
}