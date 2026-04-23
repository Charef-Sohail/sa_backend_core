package org.piteam.sa_backend_core.Mappers;

import org.piteam.sa_backend_core.dto.schedule.ScheduleCreateRequest;
import org.piteam.sa_backend_core.dto.schedule.ScheduleResponse;
import org.piteam.sa_backend_core.dto.schedule.ScheduleUpdateRequest;
import org.piteam.sa_backend_core.models.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ScheduleMapper {
    public Schedule toEntity(ScheduleCreateRequest req, String studentId) {
        return Schedule.builder()
                .studentId(studentId)
                .taskId(req.getTaskId())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .score(req.getScore())
                .source(req.getSource() != null ? req.getSource() : "MANUAL")
                .build();
    }

    public void updateEntity(ScheduleUpdateRequest req, Schedule schedule) {
        if (req.getStartTime() != null) schedule.setStartTime(req.getStartTime());
        if (req.getEndTime() != null) schedule.setEndTime(req.getEndTime());
        if (req.getScore() != null) schedule.setScore(req.getScore());
        if (req.getSource() != null) schedule.setSource(req.getSource());
    }

    public ScheduleResponse toResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .studentId(schedule.getStudentId())
                .taskId(schedule.getTaskId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .durationMinutes(schedule.getDurationMinutes())
                .score(schedule.getScore())
                .source(schedule.getSource())
                .createdAt(schedule.getCreatedAt()).updatedAt(schedule.getUpdatedAt())
                //.hasConflict(false) // Calculé dynamiquement dans le service si besoin
                .build();
    }

    // Version enrichie avec le titre de la tâche (appelée après validation)
    public ScheduleResponse toResponseWithTaskTitle(Schedule schedule, String taskTitle) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .studentId(schedule.getStudentId())
                .taskId(schedule.getTaskId())
                .taskTitle(taskTitle) // ✅ Champ enrichi
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .durationMinutes(schedule.getDurationMinutes())
                .score(schedule.getScore())
                .source(schedule.getSource())
                .createdAt(schedule.getCreatedAt()).updatedAt(schedule.getUpdatedAt())
                //.hasConflict(false)
                .build();
    }

    public List<ScheduleResponse> toResponseList(List<Schedule> schedules) {
        return schedules.stream().map(this::toResponse).toList();
    }

    public List<ScheduleResponse> toResponseListWithTaskTitle(List<Schedule> schedules, String taskTitle) {
        return schedules.stream()
                .map(schedule -> toResponseWithTaskTitle(schedule, taskTitle))
                .toList();
    }

    public List<ScheduleResponse> toResponseListWithTaskTitles(List<Schedule> schedules, Map<String, String> taskTitles) {
        return schedules.stream()
                .map(schedule -> {
                    String title = taskTitles.getOrDefault(schedule.getTaskId(), "Tâche inconnue");
                    return toResponseWithTaskTitle(schedule, title);
                })
                .toList();
    }

}
