package org.piteam.sa_backend_core.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data // Lombok génère automatiquement les getters et setters
@Document(collection = "surveys")
public class Survey {

    @Id
    private String id;

    // TRÈS IMPORTANT : Le lien vers l'étudiant
    private String studentId;

    // Les données du questionnaire
    private String wakeUpTime;
    private String sleepTime;
    private String peakProductivity;
    private int maxFocusMinutes;
    private List<String> defaultUnavailableDays;

    private LocalDateTime completedAt;

    public Survey() {
        this.completedAt = LocalDateTime.now();
    }
}
