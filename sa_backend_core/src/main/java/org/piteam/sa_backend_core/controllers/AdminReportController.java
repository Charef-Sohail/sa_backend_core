package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.Report;
import org.piteam.sa_backend_core.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    @Autowired
    private ReportRepository reportRepository;

    // 1. Voir TOUS les signalements des utilisateurs
    @GetMapping
    public ResponseEntity<?> getAllReports() {
        List<Report> reports = reportRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(reports);
    }

    // 2. Répondre à un signalement
    @PutMapping("/{id}/reply")
    public ResponseEntity<?> replyToReport(@PathVariable String id, @RequestBody Map<String, String> payload) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signalement introuvable"));

        report.setAdminReply(payload.get("reply"));
        report.setStatus("RESOLVED"); // On passe le statut à résolu

        reportRepository.save(report);
        return ResponseEntity.ok(report);
    }

    // 3. Supprimer un signalement
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id) {
        reportRepository.deleteById(id);
        return ResponseEntity.ok().body("{\"message\": \"Signalement supprimé avec succès\"}");
    }
}