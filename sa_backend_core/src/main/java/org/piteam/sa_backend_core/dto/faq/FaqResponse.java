package org.piteam.sa_backend_core.dto.faq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class FaqResponse {
    private String id;
    private String question;
    private String answer;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
}