package org.piteam.sa_backend_core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@Document(collection = "faqs")
public class Faq {

    @Id
    private String id;

    private String question;
    private String answer;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
}