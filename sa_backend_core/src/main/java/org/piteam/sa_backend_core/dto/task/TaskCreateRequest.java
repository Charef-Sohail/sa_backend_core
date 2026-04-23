package org.piteam.sa_backend_core.dto.task;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCreateRequest {
    @NotBlank(message = "L'ID étudiant est requis")
    private String studentId;

    @NotBlank(message = "Le titre est requis")
    @Size(min = 3, max = 100, message = "Le titre doit faire entre 3 et 100 caractères")
    private String title;

    private String description;

    @NotBlank
    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL", message = "Priorité invalide")
    private String priority;

    @NotNull(message = "La deadline est requise")
    @Future(message = "La deadline doit être dans le futur")
    private LocalDateTime deadline;

    @NotNull(message = "La durée est requise")
    @Min(value = 1, message = "La durée doit être supérieure à 0")
    private Integer duration;

    @NotNull(message = "La difficulté est requise")
    @Min(value = 1, message = "La difficulté doit être entre 1 et 5")
    @Max(value = 5, message = "La difficulté doit être entre 1 et 5")
    private Integer difficulty;

    @NotBlank(message = "La catégorie est requise")
    private String category;

    // Status optionnel (défaut "TODO" géré côté serveur)
    @Pattern(regexp = "TODO|IN_PROGRESS|REVIEW|DONE|CANCELLED", message = "Statut invalide")
    private String status;

    private List<String> tags;
}
