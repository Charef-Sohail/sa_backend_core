package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.ProfileCreateRequest;
import org.piteam.sa_backend_core.dto.ProfileResponse;
import org.piteam.sa_backend_core.dto.ProfileUpdateRequest;
import org.piteam.sa_backend_core.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/student/{studentId}/profile") @RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/newProfile")
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileCreateRequest request, @PathVariable String studentId) {
        ProfileResponse created = profileService.createProfile(request);
        URI location = URI.create("/api/student/"+studentId+"/profile" + created.getStudentId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String studentId) {
        return ResponseEntity.ok(profileService.getProfileByStudentId(studentId));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable String studentId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(studentId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(@PathVariable String studentId) {
        profileService.deleteProfile(studentId);
        return ResponseEntity.noContent().build();
    }
}
