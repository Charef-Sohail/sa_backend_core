//package org.piteam.sa_backend_core.controllers;
//
//import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
//import org.piteam.sa_backend_core.models.Schedule;
//import org.piteam.sa_backend_core.services.PlannerService;
//import org.piteam.sa_backend_core.repositories.ScheduleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/planner")
//@CrossOrigin(origins = "http://localhost:5173") // Autorise votre Frontend React
//public class PlannerController {
//
//    @Autowired
//    private PlannerService plannerService;
//
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//
//    /**
//     * Déclenche l'IA pour générer et sauvegarder un nouveau planning.
//     */
//    @PostMapping("/generate")
//    public ResponseEntity<?> generateAndSaveSchedule(@RequestBody OptimizationRequestDTO requestDTO, Principal principal) {
//        try {
//            // On s'assure que le studentId dans le DTO correspond bien à l'utilisateur connecté
//            requestDTO.getStudent().setStudentId(principal.getName());
//
//            Schedule savedSchedule = plannerService.createAndSaveSchedule(requestDTO);
//            return ResponseEntity.ok(savedSchedule);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Erreur lors de la génération : " + e.getMessage());
//        }
//    }
//
//    /**
//     * Récupère le dernier planning généré pour l'étudiant connecté.
//     */
//    @GetMapping("/current")
//    public ResponseEntity<?> getCurrentSchedule(Principal principal) {
//        String studentId = principal.getName();
//        Optional<Schedule> schedule = scheduleRepository.findByStudentId(studentId);
//
//        return schedule
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//}