package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.DailyState;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.piteam.sa_backend_core.services.DailyStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
public class DailyStateController {

    @Autowired
    private DailyStateService dailyStateService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> declareMood(@RequestBody DailyState dailyState, Principal principal) {
        // Extraction sécurisée de l'ID depuis le JWT
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuth.getTokenAttributes();

        String userEmail = (String) claims.get("sub");
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = (String) claims.get("email");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String studentId = user.getId();

        DailyState savedState = dailyStateService.saveOrUpdateDailyState(dailyState, studentId);

        return ResponseEntity.ok(savedState);
    }
}