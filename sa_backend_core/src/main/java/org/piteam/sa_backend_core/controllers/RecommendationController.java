package org.piteam.sa_backend_core.controllers;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.place.RecommendStoreRequest;
import org.piteam.sa_backend_core.services.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<String> recommendStore(
            @RequestBody RecommendStoreRequest request,
            Authentication authentication
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");

        String response =
                recommendationService.recommendStore(request, studentId);

        return ResponseEntity.ok(response);
    }
}
