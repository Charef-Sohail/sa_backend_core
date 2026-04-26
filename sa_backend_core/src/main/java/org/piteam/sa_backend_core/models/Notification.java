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
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String studentId;
    private String title;
    private String message;
    private boolean isRead; // Pour savoir si l'étudiant l'a lue
    private LocalDateTime createdAt;

    // Constructeur pratique
    public Notification(String studentId, String title, String message) {
        this.studentId = studentId;
        this.title = title;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
}