package org.piteam.sa_backend_core.Mappers;

import org.piteam.sa_backend_core.dto.TaskCreateRequest;
import org.piteam.sa_backend_core.dto.TaskResponse;
import org.piteam.sa_backend_core.dto.TaskUpdateRequest;
import org.piteam.sa_backend_core.models.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskMapper {
    public Task toEntity(TaskCreateRequest req) {
        return Task.builder()
                .studentId(req.getStudentId())
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority())
                .deadline(req.getDeadline())
                .duration(req.getDuration())
                .difficulty(req.getDifficulty())
                .category(req.getCategory())
                .status(req.getStatus() != null ? req.getStatus() : "TODO")
                .tags(req.getTags() != null ? req.getTags() : List.of())
                .build();
    }

    public void updateEntity(TaskUpdateRequest req, Task task) {
        if (req.getTitle() != null) task.setTitle(req.getTitle());
        if (req.getDescription() != null) task.setDescription(req.getDescription());
        if (req.getPriority() != null) task.setPriority(req.getPriority());
        if (req.getDeadline() != null) task.setDeadline(req.getDeadline());
        if (req.getDuration() != null) task.setDuration(req.getDuration());
        if (req.getDifficulty() != null) task.setDifficulty(req.getDifficulty());
        if (req.getCategory() != null) task.setCategory(req.getCategory());
        if (req.getStatus() != null) task.setStatus(req.getStatus());
        if (req.getTags() != null) task.setTags(req.getTags());
    }

    public TaskResponse toResponse(Task task) {
        boolean overdue = task.getDeadline() != null
                && task.getDeadline().isBefore(LocalDateTime.now())
                && !"DONE".equals(task.getStatus());

        return TaskResponse.builder()
                .id(task.getId()).studentId(task.getStudentId()).title(task.getTitle())
                .description(task.getDescription()).priority(task.getPriority())
                .deadline(task.getDeadline()).duration(task.getDuration())
                .difficulty(task.getDifficulty()).category(task.getCategory())
                .status(task.getStatus()).tags(task.getTags())
                .createdAt(task.getCreatedAt()).updatedAt(task.getUpdatedAt())
                .isOverdue(overdue)
                .build();
    }

    public List<TaskResponse> toResponseList(List<Task> tasks) {
        return tasks.stream().map(this::toResponse).toList();
    }
}
