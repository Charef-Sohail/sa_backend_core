package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.services.PlannerService;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/planner")
@CrossOrigin(origins = "http://localhost:5173")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAndSaveSchedule(Principal principal) {
        try {
            // Le Principal contient l'ID (ou l'email) de l'utilisateur grâce au Token JWT
            String studentId = principal.getName();

            // On lance la machine !
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