package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
import org.piteam.sa_backend_core.dto.planner.OptimizationResponseDTO;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class PlannerService {

    @Autowired
    private PlannerClientService plannerClient; // Votre service HTTP
    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> createAndSaveSchedule(OptimizationRequestDTO request) {
        // 1. Appel au microservice Python
        OptimizationResponseDTO response = plannerClient.askPythonForSchedule(request);

        if (!"SUCCESS".equals(response.getStatus())) {
            throw new RuntimeException("Échec de l'optimisation IA");
        }

        String studentId = request.getStudent().getStudentId();

        // 2. Nettoyage : On supprime l'ancien planning de l'étudiant avant d'insérer le nouveau
        scheduleRepository.deleteByStudentId(studentId);

        // 3. Création de la LISTE des nouveaux créneaux (Schedules)
        List<Schedule> schedulesToSave = response.getSchedule().stream().map(dto -> {

            // On convertit la date String de Python en LocalDateTime pour Java
            LocalDateTime start = ZonedDateTime.parse(dto.getStartTime()).toLocalDateTime();
            LocalDateTime end = ZonedDateTime.parse(dto.getEndTime()).toLocalDateTime();

            // Utilisation du @Builder de Yassine pour créer l'objet proprement
            return Schedule.builder()
                    .studentId(studentId)
                    .taskId(dto.getTaskId())
                    .startTime(start)
                    .endTime(end)
                    .source("AI_OR_TOOLS") // On précise que c'est l'IA qui a créé ce créneau
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

        }).collect(Collectors.toList());

        // 4. Sauvegarde de toute la liste dans MongoDB
        return scheduleRepository.saveAll(schedulesToSave);
    }
}