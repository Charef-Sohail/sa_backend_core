package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.DailyState;
import org.piteam.sa_backend_core.services.DailyStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/mood")
public class DailyStateController {

    @Autowired
    private DailyStateService dailyStateService;

    @PostMapping
    public ResponseEntity<?> declareMood(@RequestBody DailyState dailyState, Principal principal) {
        // principal.getName() récupère l'ID ou l'email contenu dans le Token JWT
        String studentId = principal.getName();

        DailyState savedState = dailyStateService.saveOrUpdateDailyState(dailyState, studentId);

        return ResponseEntity.ok(savedState);
    }
}