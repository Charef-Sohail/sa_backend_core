package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.RecommendationFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendationFeedbackRepository extends MongoRepository<RecommendationFeedback,String> {
    boolean existsByUserIdAndProductAndStoreId(String userId, String product, String storeId);
    boolean existsByUserIdAndProductContainingIgnoreCaseAndStoreId(
            String userId,
            String product,
            String storeId
    );
}
