package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.place.RecommendStoreRequest;
import org.piteam.sa_backend_core.models.ProductRecommendation;
import org.piteam.sa_backend_core.models.RecommendationFeedback;
import org.piteam.sa_backend_core.models.Store;
import org.piteam.sa_backend_core.repositories.ProductRecommendationRepository;
import org.piteam.sa_backend_core.repositories.RecommendationFeedbackRepository;
import org.piteam.sa_backend_core.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final StoreRepository storeRepository;

    private final ProductRecommendationRepository recommendationRepository;

    private final RecommendationFeedbackRepository feedbackRepository;

    public String recommendStore(RecommendStoreRequest request, String studentId) {

        // 1. Find store by placeId
        Store store = storeRepository
                .findByPlaceId(request.getPlaceId())
                .orElseGet(() -> {

                    // Create store if not exists
                    Store newStore = Store.builder()
                            .placeId(request.getPlaceId())
                            .name(request.getName())
                            .address(request.getAddress())
                            .latitude(request.getLatitude())
                            .longitude(request.getLongitude())
                            .phone(request.getPhone())
                            .website(request.getWebsite())
                            .url(request.getUrl())
                            .categories(request.getCategories())
                            .build();

                    return storeRepository.save(newStore);
                });

        // 2. Prevent duplicate recommendation
        boolean alreadyRecommended =
                feedbackRepository.existsByUserIdAndProductContainingIgnoreCaseAndStoreId(
                        //request.getUserId(),
                        studentId,
                        request.getProduct(),
                        store.getId()
                );

        if (alreadyRecommended) {
            return "You already recommended this store.";
        }

        // 3. Find recommendation
        ProductRecommendation recommendation =
                recommendationRepository
                        .findByProductContainingIgnoreCaseAndStoreId(
                                request.getProduct(),
                                store.getId()
                        )
                        .orElseGet(() ->
                                ProductRecommendation.builder()
                                        .product(request.getProduct())
                                        .storeId(store.getId())
                                        .count(0L)
                                        .build()
                        );

        // 4. Increment count
        recommendation.setCount(recommendation.getCount() + 1);

        recommendationRepository.save(recommendation);

        // 5. Save feedback
        RecommendationFeedback feedback =
                RecommendationFeedback.builder()
                        //.userId(request.getUserId())
                        .userId(studentId)
                        .product(request.getProduct())
                        .storeId(store.getId())
                        .createdAt(LocalDateTime.now())
                        .build();

        feedbackRepository.save(feedback);

        return "Store recommended successfully.";
    }
}
