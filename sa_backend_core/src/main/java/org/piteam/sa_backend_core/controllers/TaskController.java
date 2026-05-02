package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import org.piteam.sa_backend_core.dto.task.TaskCreateRequest;
import org.piteam.sa_backend_core.dto.task.TaskResponse;
import org.piteam.sa_backend_core.dto.task.TaskUpdateRequest;
import org.piteam.sa_backend_core.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController @RequestMapping("/api/student/tasks")
//@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");
        TaskResponse created = taskService.createTask(request, studentId);
        URI location = URI.create("/api/student/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");
        return ResponseEntity.ok(taskService.getTaskById(taskId, studentId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getStudentTasks(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");
        return ResponseEntity.ok(taskService.getTasksByStudent(studentId));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskUpdateRequest request, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");
        return ResponseEntity.ok(taskService.updateTask(taskId, request, studentId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        String studentId = jwt.getClaim("id");
        taskService.deleteTask(taskId, studentId);
        return ResponseEntity.noContent().build();
    }

}
