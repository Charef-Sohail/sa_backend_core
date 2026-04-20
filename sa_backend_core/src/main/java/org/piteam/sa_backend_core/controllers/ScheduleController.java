package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.ScheduleCreateRequest;
import org.piteam.sa_backend_core.dto.ScheduleResponse;
import org.piteam.sa_backend_core.dto.ScheduleUpdateRequest;
import org.piteam.sa_backend_core.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/student/{studentId}/schedules") @RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;


    @PostMapping("/newSchedule")
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request, @PathVariable String studentId) {
        ScheduleResponse created = scheduleService.createSchedule(request);
        URI location = URI.create("/api/student/"+studentId+"/schedules/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getStudentSchedules(@PathVariable String studentId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStudent(studentId));
    }

//    @GetMapping("/task/{taskId}")
//    public ResponseEntity<List<ScheduleResponse>> getTaskSchedules(@PathVariable String taskId) {
//        return ResponseEntity.ok(scheduleService.getSchedulesByTask(taskId));
//    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ScheduleResponse>> getStudentTaskSchedules(@PathVariable String taskId,  @PathVariable String studentId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStudentAndTask(studentId, taskId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable String id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable String id,
            @Valid @RequestBody ScheduleUpdateRequest request,
            @PathVariable String studentId) { // 🔐 Passé en paramètre (à remplacer par @AuthenticationPrincipal en prod avec @RequestParam)
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request, studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id, @PathVariable String studentId) {
        scheduleService.deleteSchedule(id, studentId);
        return ResponseEntity.noContent().build();
    }
}
