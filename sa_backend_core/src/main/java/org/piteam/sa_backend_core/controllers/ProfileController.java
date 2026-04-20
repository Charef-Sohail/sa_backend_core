package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.ProfileCreateRequest;
import org.piteam.sa_backend_core.dto.ProfileResponse;
import org.piteam.sa_backend_core.dto.ProfileUpdateRequest;
import org.piteam.sa_backend_core.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/profile") @RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileCreateRequest request, Authentication authentication) {
        String studentId = authentication.getName();
        ProfileResponse created = profileService.createProfile(request, studentId);
        URI location = URI.create("/api/profile" + created.getStudentId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(profileService.getProfileByStudentId(studentId));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(profileService.updateProfile(studentId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String  studentId = authentication.getName();
        profileService.deleteProfile(studentId);
        return ResponseEntity.noContent().build();
    }
}
