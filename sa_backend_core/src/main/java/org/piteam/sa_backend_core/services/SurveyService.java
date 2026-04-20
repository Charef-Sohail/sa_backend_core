package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.models.Survey;
import org.piteam.sa_backend_core.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    public Survey saveOrUpdateSurvey(Survey surveyRequest, String studentId) {
        // On cherche si l'étudiant a déjà rempli un survey
        Optional<Survey> existingSurvey = surveyRepository.findByStudentId(studentId);

        if (existingSurvey.isPresent()) {
            // S'il existe, on met à jour uniquement les réponses
            Survey surveyToUpdate = existingSurvey.get();
            surveyToUpdate.setWakeUpTime(surveyRequest.getWakeUpTime());
            surveyToUpdate.setSleepTime(surveyRequest.getSleepTime());
            surveyToUpdate.setPeakProductivity(surveyRequest.getPeakProductivity());
            surveyToUpdate.setMaxFocusMinutes(surveyRequest.getMaxFocusMinutes());
            surveyToUpdate.setDefaultUnavailableDays(surveyRequest.getDefaultUnavailableDays());

            // On actualise la date de modification
            surveyToUpdate.setCompletedAt(LocalDateTime.now());

            return surveyRepository.save(surveyToUpdate);
        }

        // S'il n'existe pas, on lie l'ID de l'étudiant et on sauvegarde le nouveau
        surveyRequest.setStudentId(studentId);
        return surveyRepository.save(surveyRequest);
    }
}