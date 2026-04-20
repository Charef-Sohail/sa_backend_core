package org.piteam.sa_backend_core.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String studentId;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime deadline;
    private Integer duration;
    private Integer difficulty;
    private String category;
    private String status;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Champ calculé métier (jamais stocké en base)
    private Boolean isOverdue;
}
