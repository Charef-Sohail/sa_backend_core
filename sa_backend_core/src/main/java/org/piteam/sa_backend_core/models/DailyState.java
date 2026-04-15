package org.piteam.sa_backend_core.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "daily_states")
public class DailyState {

    @Id
    private String id;

    // Le lien vers l'étudiant
    private String studentId;

    private LocalDate date; // Ex: 2024-05-20 (Une seule entrée par jour)

    // Valeurs cruciales pour l'IA
    private int energyScore; // De 1 (Épuisé) à 5 (En pleine forme)
    private String moodType; // "MOTIVATED", "NORMAL", "TIRED", "STRESSED"

    private String note; // Optionnel (ex: "Nuit blanche à réviser")

    private LocalDateTime recordedAt;

    public DailyState() {
        this.recordedAt = LocalDateTime.now();
        this.date = LocalDate.now(); // On fixe automatiquement la date du jour
    }
}