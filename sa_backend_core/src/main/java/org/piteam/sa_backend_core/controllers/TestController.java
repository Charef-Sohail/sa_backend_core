package org.piteam.sa_backend_core.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // anyone authenticated can access this
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> whoAmI(Authentication authentication) {

        String authorities = authentication.getAuthorities() != null
                ? authentication.getAuthorities().toString()
                : "none";

        boolean isAdmin = authentication.getAuthorities() != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isStudent = authentication.getAuthorities() != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        Map<String, Object> response = new HashMap<>();
        response.put("email", authentication.getName());
        response.put("authorities", authorities);
        response.put("isAdmin", isAdmin);
        response.put("isStudent", isStudent);

        return ResponseEntity.ok(response);
    }

    // only admin can access this
    @GetMapping("/admin-only")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> adminOnly() {
        return ResponseEntity.ok(Map.of("message", "You are an ADMIN"));
    }

    // only student can access this
    @GetMapping("/student-only")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Map<String, String>> studentOnly() {
        return ResponseEntity.ok(Map.of("message", "You are a STUDENT"));
    }
}