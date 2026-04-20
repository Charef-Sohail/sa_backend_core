package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.Survey;
import org.piteam.sa_backend_core.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public ResponseEntity<?> submitSurvey(@RequestBody Survey surveyRequest, Principal principal) {

        // On extrait l'ID de l'étudiant depuis le token JWT
        String studentId = principal.getName();

        // On passe le relais au Service
        Survey savedSurvey = surveyService.saveOrUpdateSurvey(surveyRequest, studentId);

        return ResponseEntity.ok(savedSurvey);
    }
}