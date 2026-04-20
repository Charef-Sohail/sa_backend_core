package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import org.piteam.sa_backend_core.dto.TaskCreateRequest;
import org.piteam.sa_backend_core.dto.TaskResponse;
import org.piteam.sa_backend_core.dto.TaskUpdateRequest;
import org.piteam.sa_backend_core.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController @RequestMapping("/api/tasks")
//@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request, Authentication authentication) {
        String studentId = authentication.getName();
        TaskResponse created = taskService.createTask(request, studentId);
        URI location = URI.create("/api/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId, Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(taskService.getTaskById(taskId, userId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getStudentTasks(Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksByStudent(studentId));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskUpdateRequest request, Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(taskService.updateTask(taskId, request, studentId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId, Authentication authentication) {
        String studentId = authentication.getName();
        taskService.deleteTask(taskId, studentId);
        return ResponseEntity.noContent().build();
    }

}
