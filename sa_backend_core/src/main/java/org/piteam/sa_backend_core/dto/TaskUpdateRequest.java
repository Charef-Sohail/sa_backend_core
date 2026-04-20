package org.piteam.sa_backend_core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskUpdateRequest {
    @Size(min = 3, max = 100, message = "Le titre doit faire entre 3 et 100 caractères")
    private String title;

    private String description;

    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL", message = "Priorité invalide")
    private String priority;

    @Future(message = "La deadline doit être dans le futur")
    private LocalDateTime deadline;

    @Min(value = 1, message = "La durée doit être supérieure à 0")
    private Integer duration;

    @Min(value = 1, message = "La difficulté doit être entre 1 et 5")
    @Max(value = 5, message = "La difficulté doit être entre 1 et 5")
    private Integer difficulty;

    private String category;

    @Pattern(regexp = "TODO|IN_PROGRESS|REVIEW|DONE|CANCELLED", message = "Statut invalide")
    private String status;

    private List<String> tags;
}
