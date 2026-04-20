package org.piteam.sa_backend_core.services;

//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.Mappers.TaskMapper;
import org.piteam.sa_backend_core.dto.TaskCreateRequest;
import org.piteam.sa_backend_core.dto.TaskResponse;
import org.piteam.sa_backend_core.dto.TaskUpdateRequest;
import org.piteam.sa_backend_core.exceptions.ResourceNotFoundException;
import org.piteam.sa_backend_core.models.Task;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.piteam.sa_backend_core.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;
    private final TaskMapper taskMapper;


    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        //log.info("Création tâche: {}", request.getTitle());
        Task saved = taskRepository.save(taskMapper.toEntity(request));
        return taskMapper.toResponse(saved);
    }

    public TaskResponse getTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche  non trouvée " + id));
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getTasksByStudent(String studentId) {
        return taskMapper.toResponseList(taskRepository.findByStudentId(studentId));
    }

    @Transactional
    public TaskResponse updateTask(String id, TaskUpdateRequest request) {
        //log.info("Mise à jour tâche: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée "+ id));
        taskMapper.updateEntity(request, task);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(String id) {
        //log.info("Suppression cascade tâche: {}", id);
        if (!taskRepository.existsById(id))
            throw new ResourceNotFoundException("Tâche non trouvée "+ id);

        scheduleRepository.deleteByTaskId(id); // Supprime les schedules liés
        taskRepository.deleteById(id);
    }
}
