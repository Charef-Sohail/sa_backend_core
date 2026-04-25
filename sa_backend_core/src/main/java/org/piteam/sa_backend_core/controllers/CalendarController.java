package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.piteam.sa_backend_core.services.IcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173")
public class CalendarController {

    @Autowired
    private IcsService icsService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/import")
    public ResponseEntity<?> uploadCalendar(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier est vide.");
            }

            // 1. On "cast" le Principal pour lire le vrai contenu du JWT
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
            Map<String, Object> claims = jwtAuth.getTokenAttributes();

            // Affichez ceci dans la console pour voir TOUT ce que contient le token
            System.out.println("Contenu du Token JWT : " + claims);

            // 2. On extrait l'identifiant (il s'appelle souvent "sub" ou "email")
            String userEmail = (String) claims.get("sub");
            if (userEmail == null || userEmail.isEmpty()) {
                userEmail = (String) claims.get("email");
            }

            if (userEmail == null) {
                return ResponseEntity.badRequest().body("Impossible de trouver l'email dans le Token JWT.");
            }

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String studentId = user.getId();
            return ResponseEntity.ok(icsService.importIcsCalendar(file, studentId));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'importation : " + e.getMessage());
        }
    }
}