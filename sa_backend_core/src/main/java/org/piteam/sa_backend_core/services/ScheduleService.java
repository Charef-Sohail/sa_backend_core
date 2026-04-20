package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.Mappers.ScheduleMapper;
import org.piteam.sa_backend_core.dto.ScheduleCreateRequest;
import org.piteam.sa_backend_core.dto.ScheduleResponse;
import org.piteam.sa_backend_core.dto.ScheduleUpdateRequest;
import org.piteam.sa_backend_core.exceptions.ResourceNotFoundException;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.models.Task;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.piteam.sa_backend_core.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request, String studentId) {

        // Validation : la tâche existe ET appartient au même étudiant
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée "+ request.getTaskId()));

        if (!task.getStudentId().equals(studentId)) {
            throw new SecurityException("Le planning ne peut être associé à une tâche d'un autre étudiant");
        }

        // Création
        Schedule schedule = scheduleMapper.toEntity(request, studentId);
        if(!schedule.isTimeRangeValid()){
            throw new IllegalStateException("TimeRange non valid");
        }

        // Validation : somme des durées ne dépasse pas task.duration
        validateTotalDurationNotExceeded(task, schedule.getDurationMinutes(), null);

        // Validation : pas de chevauchement de créneaux pour cet étudiant
        List<Schedule> conflicts = scheduleRepository.findOverlappingSchedules(
                schedule.getStudentId(), schedule.getStartTime(), schedule.getEndTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Ce créneau chevauche un planning existant");
        }
        Schedule saved = scheduleRepository.save(schedule);
        return scheduleMapper.toResponseWithTaskTitle(saved, task.getTitle());
    }

    public List<ScheduleResponse> getSchedulesByStudent(String studentId) {
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);

        if (schedules.isEmpty()) {
            return List.of();
        }

        List<String> uniqueTaskIds = schedules.stream()
                .map(Schedule::getTaskId)
                .distinct()
                .toList();

        List<Task> tasks = taskRepository.findTitleAndStudentByIdIn(uniqueTaskIds);
        Map<String, String> taskTitles = tasks.stream()
                .collect(Collectors.toMap(Task::getId, Task::getTitle));

        return scheduleMapper.toResponseListWithTaskTitles(schedules, taskTitles);
    }

    public ScheduleResponse getScheduleByIdAndStudentId(String id, String studentId) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule non trouvé "+ id));
        if (!schedule.getStudentId().equals(studentId)) {
            throw new SecurityException("Accès refusé : ce planning ne vous appartient pas");
        }
        Task task = taskRepository.findById(schedule.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée "+ schedule.getTaskId()));
        return scheduleMapper.toResponseWithTaskTitle(schedule, task.getTitle());
    }

    public  List<ScheduleResponse> getSchedulesByTask (String taskId) {
        return scheduleRepository.findByTaskId(taskId).stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    public  List<ScheduleResponse> getSchedulesByStudentAndTask (String studentId, String taskId) {
        return scheduleRepository.findByStudentIdAndTaskId(studentId, taskId).stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Transactional
    public ScheduleResponse updateSchedule(String id, ScheduleUpdateRequest request, String studentId) {

        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule non trouvée " + id));

        // Sécurité : on ne modifie que ses propres plannings
        if (!existing.getStudentId().equals(studentId)) {
            throw new SecurityException("Accès refusé : ce planning ne vous appartient pas");
        }
        Task task = taskRepository.findById(existing.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Tâche no trouvée " + existing.getTaskId()));


        scheduleMapper.updateEntity(request, existing);
        if(!existing.isTimeRangeValid()){
            throw new IllegalStateException("TimeRange non valid");
        }

        validateTotalDurationNotExceeded(task, existing.getDurationMinutes(), existing.getId());

        // Vérification chevauchement (exclure le planning en cours de mise à jour)
        List<Schedule> conflicts = scheduleRepository.findOverlappingSchedules(
                        studentId, existing.getStartTime(), existing.getEndTime()).stream()
                .filter(s -> !s.getId().equals(id))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Ce nouveau créneau chevauche un planning existant");
        }

        Schedule updated = scheduleRepository.save(existing);
        return scheduleMapper.toResponseWithTaskTitle(updated, task.getTitle());
    }

    @Transactional
    public void deleteSchedule(String id, String studentId) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule non trouvé "+ id));

        if (!schedule.getStudentId().equals(studentId)) {
            throw new SecurityException("Accès refusé : ce planning ne vous appartient pas");
        }

        scheduleRepository.deleteById(id);
    }

    // === Méthode de validation centrale ===
    private void validateTotalDurationNotExceeded(Task task, long additionalMinutes, String excludeScheduleId) {

        List<Schedule> schedules = scheduleRepository.findSchedulesWithTimesByTaskId(task.getId());
        long currentTotal = schedules.stream()
                .filter(s -> excludeScheduleId == null || !s.getId().equals(excludeScheduleId))
                .mapToLong(s -> s.getDurationMinutes())
                .sum();

        long projectedTotal = currentTotal + additionalMinutes;

        if (projectedTotal > task.getDuration()) {
            throw new IllegalArgumentException(
                    String.format("La somme des plannings (%d min) ne peut pas dépasser la durée de la tâche (%d min). " + "Dépassement de %d min.", projectedTotal, task.getDuration(), projectedTotal - task.getDuration()));
        }
    }
}
