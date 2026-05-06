package org.piteam.sa_backend_core.dto.faq;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class FaqQuestionRequest {
    @NotBlank(message = "Question is required")
    private String question;
}