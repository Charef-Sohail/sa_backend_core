package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.Notification;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.NotificationRepository;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // --- Méthode utilitaire pour extraire le vrai ID de l'étudiant ---
    private String getStudentIdFromPrincipal(Principal principal) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuth.getTokenAttributes();

        String userEmail = (String) claims.get("sub");
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = (String) claims.get("email");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return user.getId();
    }

    // --- 1. Récupérer toutes les notifications NON LUES ---
    @GetMapping
    public ResponseEntity<?> getUnreadNotifications(Principal principal) {
        try {
            String studentId = getStudentIdFromPrincipal(principal);
            List<Notification> unreadNotifs = notificationRepository.findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(studentId);
            return ResponseEntity.ok(unreadNotifs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des notifications : " + e.getMessage());
        }
    }

    // --- 2. Marquer une notification comme LUE ---
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        try {
            Notification notif = notificationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Notification introuvable"));

            notif.setRead(true); // Avec Lombok, le setter d'un 'boolean isRead' devient souvent 'setRead()'
            notificationRepository.save(notif);

            return ResponseEntity.ok("Notification marquée comme lue.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}