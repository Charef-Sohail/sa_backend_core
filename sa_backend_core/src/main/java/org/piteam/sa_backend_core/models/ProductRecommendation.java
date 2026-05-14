package org.piteam.sa_backend_core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "productRecommendations")
public class ProductRecommendation {

    @Id
    private String id;
    private String product;
    private String storeId;
    private Long count;
}
