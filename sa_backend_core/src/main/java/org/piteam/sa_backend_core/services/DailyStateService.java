package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.models.DailyState;
import org.piteam.sa_backend_core.repositories.DailyStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyStateService {

    @Autowired
    private DailyStateRepository dailyStateRepository;

    public DailyState saveOrUpdateDailyState(DailyState newState, String studentId) {
        LocalDate today = LocalDate.now();

        // Vérifie si un état existe déjà pour aujourd'hui
        Optional<DailyState> existingState = dailyStateRepository.findByStudentIdAndDate(studentId, today);

        if (existingState.isPresent()) {
            // Mise à jour
            DailyState stateToUpdate = existingState.get();
            stateToUpdate.setEnergyScore(newState.getEnergyScore());
            stateToUpdate.setMoodType(newState.getMoodType());
            stateToUpdate.setNote(newState.getNote());
            stateToUpdate.setRecordedAt(newState.getRecordedAt()); // Met à jour l'heure exacte
            return dailyStateRepository.save(stateToUpdate);
        }

        // Création
        newState.setStudentId(studentId);
        return dailyStateRepository.save(newState);
    }
}