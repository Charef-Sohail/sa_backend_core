package org.piteam.sa_backend_core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recommendationFeedbacks")
public class RecommendationFeedback {

    @Id
    private String id;
    private String userId;
    private String product;
    private String storeId;
    @CreatedDate
    private LocalDateTime createdAt;
}
