package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.models.User;
import org.piteam.sa_backend_core.repositories.UserRepository;
import org.piteam.sa_backend_core.services.PlannerService;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
@CrossOrigin(origins = "http://localhost:5173")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAndSaveSchedule(Principal principal) {
        try {
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

            // 4. On lance l'intelligence artificielle avec le BON identifiant !
            List<Schedule> savedSchedules = plannerService.generateForStudent(studentId);
            return ResponseEntity.ok(savedSchedules);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la génération : " + e.getMessage());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSchedule(Principal principal) {
        String studentId = principal.getName();

        // On récupère la liste complète du planning
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);

        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }
}