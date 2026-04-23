package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.models.SurveyQuestion;
import org.piteam.sa_backend_core.repositories.SurveyQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey-questions")
@CrossOrigin(origins = "http://localhost:5173")
public class SurveyQuestionController {

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @GetMapping
    public ResponseEntity<List<SurveyQuestion>> getQuestions() {
        List<SurveyQuestion> questions = surveyQuestionRepository.findAllByOrderByDisplayOrderAsc();
        return ResponseEntity.ok(questions);
    }

    @PostMapping
    public ResponseEntity<List<SurveyQuestion>> createQuestions(@RequestBody List<SurveyQuestion> questions) {
        List<SurveyQuestion> saved = surveyQuestionRepository.saveAll(questions);
        return ResponseEntity.status(201).body(saved);
    }
}