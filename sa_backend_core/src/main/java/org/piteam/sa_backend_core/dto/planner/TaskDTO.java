package org.piteam.sa_backend_core.dto.planner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDTO {

    private String id;
    private String title;

    @JsonProperty("duration_minutes")
    private int durationMinutes;

    private int difficulty;
    private String priority;
    private String deadline; // On peut utiliser String ici pour simplifier l'envoi ISO-8601

    // NOUVEAUX CHAMPS POUR L'IMPORT ICS
    private boolean isFixed;
    private String startTime; // Requis uniquement si isFixed = true
}