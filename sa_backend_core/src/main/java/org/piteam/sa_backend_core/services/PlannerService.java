package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.dto.planner.OptimizationRequestDTO;
import org.piteam.sa_backend_core.dto.planner.OptimizationResponseDTO;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlannerService {

    @Autowired
    private PlannerClientService plannerClient; // Votre service HTTP
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule createAndSaveSchedule(OptimizationRequestDTO request) {
        // 1. Appel au microservice Python
        OptimizationResponseDTO response = plannerClient.askPythonForSchedule(request);

        if (!"SUCCESS".equals(response.getStatus())) {
            throw new RuntimeException("Échec de l'optimisation IA");
        }

        // 2. Conversion de la réponse en entité de base de données
        Schedule newSchedule = new Schedule();
        newSchedule.setStudentId(request.getStudent().getStudentId());
        newSchedule.setGeneratedAt(LocalDateTime.now());

        List<Schedule.PlannedTask> tasks = response.getSchedule().stream().map(dto -> {
            Schedule.PlannedTask pt = new Schedule.PlannedTask();
            pt.setTaskId(dto.getTaskId());
            pt.setStartTime(dto.getStartTime());
            pt.setEndTime(dto.getEndTime());
            return pt;
        }).collect(Collectors.toList());

        newSchedule.setPlannedTasks(tasks);

        // 3. Sauvegarde automatique dans MongoDB
        return scheduleRepository.save(newSchedule);
    }
}