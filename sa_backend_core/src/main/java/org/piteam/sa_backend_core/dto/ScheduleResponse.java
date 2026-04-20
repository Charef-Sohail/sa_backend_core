package org.piteam.sa_backend_core.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class ScheduleResponse {
    private String id;
    private String studentId;
    private String taskId;
    private String taskTitle; // Champ enrichi via jointure applicative
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationMinutes; // Champ calculé
    private Float score;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //private Boolean hasConflict; // Indicateur métier : chevauchement détecté
}
