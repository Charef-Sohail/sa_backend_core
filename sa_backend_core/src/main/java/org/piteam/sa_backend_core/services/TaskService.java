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
    public TaskResponse createTask(TaskCreateRequest request, String studentId) {
        Task saved = taskRepository.save(taskMapper.toEntity(request, studentId));
        return taskMapper.toResponse(saved);
    }

    public TaskResponse getTaskById(String id, String studentId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche  non trouvée " + id));
        if(!task.getStudentId().equals(studentId)){
            throw new SecurityException("Accès refusé : cette tâche ne vous appartient pas");
        }
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getTasksByStudent(String studentId) {
        return taskMapper.toResponseList(taskRepository.findByStudentId(studentId));
    }

    @Transactional
    public TaskResponse updateTask(String id, TaskUpdateRequest request, String studentId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée "+ id));
        if(!task.getStudentId().equals(studentId)){
            throw new SecurityException("Accès refusé : cette tâche ne vous appartient pas");
        }
        taskMapper.updateEntity(request, task);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(String id, String studentId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tâche non trouvée "+ id));
        if(!task.getStudentId().equals(studentId)){
            throw new SecurityException("Accès refusé : cette tâche ne vous appartient pas");
        }
        scheduleRepository.deleteByTaskId(id); // Supprime les schedules liés
        taskRepository.deleteById(id);
    }
}
