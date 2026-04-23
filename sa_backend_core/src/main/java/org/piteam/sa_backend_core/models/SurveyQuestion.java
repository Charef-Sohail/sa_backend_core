package org.piteam.sa_backend_core.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Data
@Document(collection = "survey_questions")
public class SurveyQuestion {

    @Id
    private String id;

    // La question affichée à l'étudiant (ex: "À quelle heure vous réveillez-vous ?")
    private String text;

    // Le type d'input (ex: "TIME", "SELECT", "NUMBER") pour aider le Frontend
    private String type;

    // La clé technique de liaison (TRÈS IMPORTANT) (ex: "wakeUpTime")
    private String key;

    // Les choix possibles si type = "SELECT" (ex: ["MORNING", "AFTERNOON", "NIGHT"])
    private List<String> options;

    // L'ordre d'affichage de la question (1, 2, 3...)
    private int displayOrder;
}