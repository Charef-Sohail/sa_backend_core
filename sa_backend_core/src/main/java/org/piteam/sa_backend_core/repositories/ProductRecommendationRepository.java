package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.ProductRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRecommendationRepository extends MongoRepository<ProductRecommendation,String> {
    Optional<ProductRecommendation> findByProductAndStoreId(String product, String storeId);
    List<ProductRecommendation> findByProduct(String product);
    Optional<ProductRecommendation> findByProductContainingIgnoreCaseAndStoreId(String product, String storeId);
    List<ProductRecommendation> findByProductContainingIgnoreCase(String product);
}
