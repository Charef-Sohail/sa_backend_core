package org.piteam.sa_backend_core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String studentId;

    // Le message envoyé par l'étudiant
    private String message;

    // La réponse donnée par l'administrateur
    private String adminReply;

    // Statut : "PENDING" (En attente), "RESOLVED" (Résolu)
    private String status;

    private LocalDateTime createdAt;

    public Report(String studentId, String message) {
        this.studentId = studentId;
        this.message = message;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}