package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.Survey;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.piteam.sa_backend_core.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> submitSurvey(@RequestBody Survey surveyRequest, Principal principal) {

        // 1. On "cast" le Principal pour lire le JWT correctement
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuth.getTokenAttributes();

        // 2. On extrait l'email
        String userEmail = (String) claims.get("sub");
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = (String) claims.get("email");
        }

        // 3. On récupère le vrai ID MongoDB de l'étudiant
        String finalUserEmail = userEmail;
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + finalUserEmail));

        String studentId = user.getId();
        // On passe le relais au Service
        Survey savedSurvey = surveyService.saveOrUpdateSurvey(surveyRequest, studentId);

        return ResponseEntity.ok(savedSurvey);
    }
}