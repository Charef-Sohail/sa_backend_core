package org.piteam.sa_backend_core.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.admin.AdminResponse;
import org.piteam.sa_backend_core.dto.admin.AdminUpdateUserRequest;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody AdminUpdateUserRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }
}