package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.Report;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.ReportRepository;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    // Utilitaire pour extraire l'ID du JWT
    private String getUserIdFromPrincipal(Principal principal) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuth.getTokenAttributes();
        String userEmail = (String) claims.get("sub");
        if (userEmail == null) userEmail = (String) claims.get("email");

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return user.getId();
    }

    // 1. Créer un nouveau signalement
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody Map<String, String> payload, Principal principal) {
        String studentId = getUserIdFromPrincipal(principal);
        String message = payload.get("message");

        Report report = new Report(studentId, message);
        reportRepository.save(report);

        return ResponseEntity.ok(report);
    }

    // 2. Voir ses propres signalements
    @GetMapping("/my-reports")
    public ResponseEntity<?> getMyReports(Principal principal) {
        String studentId = getUserIdFromPrincipal(principal);
        List<Report> myReports = reportRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
        return ResponseEntity.ok(myReports);
    }
}