package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.schedule.ScheduleCreateRequest;
import org.piteam.sa_backend_core.dto.schedule.ScheduleResponse;
import org.piteam.sa_backend_core.dto.schedule.ScheduleUpdateRequest;
import org.piteam.sa_backend_core.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/api/schedules") @RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;


    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request, Authentication authentication) {
        String studentId = authentication.getName();
        ScheduleResponse created = scheduleService.createSchedule(request, studentId);
        URI location = URI.create("/api/schedules/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getStudentSchedules(Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(scheduleService.getSchedulesByStudent(studentId));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ScheduleResponse>> getStudentTaskSchedules(@PathVariable String taskId,  Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(scheduleService.getSchedulesByStudentAndTask(studentId, taskId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable String id, Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(scheduleService.getScheduleByIdAndStudentId(id, studentId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable String id,
            @Valid @RequestBody ScheduleUpdateRequest request,
            Authentication authentication) {
        String studentId = authentication.getName();
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request, studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id, Authentication authentication) {
        String studentId = authentication.getName();
        scheduleService.deleteSchedule(id, studentId);
        return ResponseEntity.noContent().build();
    }
}
