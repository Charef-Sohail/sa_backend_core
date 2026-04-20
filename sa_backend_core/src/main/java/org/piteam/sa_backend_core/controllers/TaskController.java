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

@RestController @RequestMapping("/api/student/{studentId}/tasks")
//@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/newTask")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request, @PathVariable String studentId) {
        TaskResponse created = taskService.createTask(request);
        URI location = URI.create("/api/student/"+ studentId +"/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getStudentTasks(@PathVariable String studentId) {
        return ResponseEntity.ok(taskService.getTasksByStudent(studentId));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
