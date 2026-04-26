package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.dto.planner.*;
import org.piteam.sa_backend_core.models.*;
import org.piteam.sa_backend_core.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    @Autowired
    private PlannerClientService plannerClient;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private DailyStateRepository dailyStateRepository;

    public List<Schedule> generateForStudent(String studentId) {
        // 1. On cherche le profil de l'étudiant (Heure de réveil, etc.)
        Survey survey = surveyRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Veuillez d'abord remplir votre profil (Survey)."));

        // 2. On récupère toutes les tâches de l'étudiant qui ne sont pas encore terminées
        List<Task> studentTasks = taskRepository.findByStudentId(studentId).stream()
                .filter(t -> !"COMPLETED".equals(t.getStatus()))
                .collect(Collectors.toList());

        if (studentTasks.isEmpty()) {
            throw new RuntimeException("Vous n'avez aucune tâche en cours à planifier !");
        }

        // 3. On emballe le tout dans notre DTO pour Python
        OptimizationRequestDTO request = buildRequest(studentId, survey, studentTasks);

        // 4. Appel de l'IA
        OptimizationResponseDTO response = plannerClient.askPythonForSchedule(request);

        if (!"SUCCESS".equals(response.getStatus())) {
            throw new RuntimeException("L'IA n'a pas pu générer le planning : " + response.getMessage());
        }

        // 5. Nettoyage de l'ancien calendrier et sauvegarde du nouveau
        scheduleRepository.deleteByStudentId(studentId);

        List<Schedule> toSave = response.getSchedule().stream().map(dto -> {
            return Schedule.builder()
                    .studentId(studentId)
                    .taskId(dto.getTaskId())
                    .startTime(ZonedDateTime.parse(dto.getStartTime()).toLocalDateTime())
                    .endTime(ZonedDateTime.parse(dto.getEndTime()).toLocalDateTime())
                    .source("AI_OR_TOOLS")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        return scheduleRepository.saveAll(toSave);
    }

    // Méthode utilitaire pour convertir les Entités de la Base en DTO pour l'IA
    private OptimizationRequestDTO buildRequest(String studentId, Survey survey, List<Task> tasks) {
        OptimizationRequestDTO request = new OptimizationRequestDTO();

        StudentProfileDTO profileDTO = new StudentProfileDTO();
        profileDTO.setStudentId(studentId);
        profileDTO.setWakeUpTime(survey.getWakeUpTime());
        profileDTO.setSleepTime(survey.getSleepTime());
        profileDTO.setPeakProductivity(survey.getPeakProductivity());

        // --- NOUVELLE LOGIQUE MOOD TRACKING ---
        // On cherche si l'étudiant a déclaré son humeur AUJOURD'HUI
        Optional<DailyState> todayMood = dailyStateRepository.findByStudentIdAndDate(studentId, LocalDate.now());

        if (todayMood.isPresent()) {
            // Si oui, on prend l'énergie d'aujourd'hui (1 à 5)
            profileDTO.setEnergyScore(todayMood.get().getEnergyScore());
        } else {
            // Sinon, on met 3 par défaut (Énergie normale)
            profileDTO.setEnergyScore(3);
        }

        request.setStudent(profileDTO);

        // Mapping des tâches
        List<TaskDTO> taskDTOs = tasks.stream().map(t -> {
            TaskDTO d = new TaskDTO();
            d.setId(t.getId());
            d.setTitle(t.getTitle());

            // Sécurité pour la durée
            d.setDurationMinutes(t.getDuration() != null ? t.getDuration() : 60);

            // Sécurité pour la difficulté (Si c'est null, on met 1 par défaut)
            d.setDifficulty(t.getDifficulty() != null ? t.getDifficulty() : 1);

            // Sécurité pour la priorité (Si c'est null, on met NORMAL par défaut)
            d.setPriority(t.getPriority() != null ? t.getPriority() : "NORMAL");

            d.setDeadline(t.getDeadline() != null ? t.getDeadline().toString() : ZonedDateTime.now().plusDays(7).toString());

            // --- LOGIQUE POUR LES COURS FIXES ---
            boolean isFixed = t.getTags() != null && t.getTags().contains("FIXED_EVENT");
            d.setFixed(isFixed);

            if (isFixed && t.getDeadline() != null) {
                LocalDateTime endTime = t.getDeadline();
                // Assurez-vous d'utiliser la durée sécurisée ici aussi !
                int duration = t.getDuration() != null ? t.getDuration() : 60;
                LocalDateTime startTime = endTime.minusMinutes(duration);
                d.setStartTime(startTime.atZone(ZoneId.systemDefault()).toString());
            }

            return d;
        }).collect(Collectors.toList());

        request.setTasks(taskDTOs);
        request.setStartDate(ZonedDateTime.now()); // Le planning commence à partir de maintenant !

        return request;
    }
}