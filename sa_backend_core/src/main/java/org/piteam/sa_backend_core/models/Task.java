package org.piteam.sa_backend_core.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Data @AllArgsConstructor @NoArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    @NotBlank(message = "L'ID étudiant est requis")
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
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
