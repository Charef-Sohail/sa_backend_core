package org.piteam.sa_backend_core.services;


import org.piteam.sa_backend_core.dto.place.PlaceDTO;
import org.piteam.sa_backend_core.dto.place.PlaceSearchRequest;
import org.piteam.sa_backend_core.dto.place.ProductSearchResult;
import org.piteam.sa_backend_core.models.ProductRecommendation;
import org.piteam.sa_backend_core.models.Store;
import org.piteam.sa_backend_core.repositories.ProductRecommendationRepository;
import org.piteam.sa_backend_core.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.*;

@Service
public class PlaceSearchService {

    @Value("${node.service.url:http://localhost:3000}")
    private String nodeServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRecommendationRepository recommendationRepository;

    @Autowired
    private StoreRepository storeRepository;

    public ProductSearchResult searchNearbyPlaces(PlaceSearchRequest request) {
        String url = nodeServiceUrl + "/api/search-places";

        Map<String, Object> payload = new HashMap<>();
        payload.put("product", request.getProduct());
        payload.put("latitude", request.getLatitude());
        payload.put("longitude", request.getLongitude());
        payload.put("maxResults", request.getMaxResults());

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, payload, JsonNode.class);

            if (response.getBody() != null && response.getBody().has("success") && response.getBody().get("success").asBoolean()) {
                JsonNode priceInfo = response.getBody().get("priceInfo");
                //String price = "";
                String price = priceInfo.toString();

                List<ProductRecommendation> recommendations = recommendationRepository.findByProductContainingIgnoreCase(request.getProduct());
                // Map: placeId -> recommendation count
                Map<String, Long> recommendationMap = new HashMap<>();

                for (ProductRecommendation recommendation : recommendations) {

                    Optional<Store> storeOptional = storeRepository.findById(recommendation.getStoreId());

                    if (storeOptional.isPresent()) {

                        Store store = storeOptional.get();

                        recommendationMap.put(store.getPlaceId(), recommendation.getCount());
                    }
                }
                
                //String price ="No Price Found";
                JsonNode placesNode = response.getBody().get("places");
                List<PlaceDTO> places = new ArrayList<>();

                if (placesNode.isArray()) {
                    for (JsonNode placeNode : placesNode) {
                        PlaceDTO place = new PlaceDTO();
                        String placeId = placeNode.has("id") ? placeNode.get("id").asText() : null;
                        place.setId(placeId);
                        //place.setId(placeNode.has("id") ? placeNode.get("id").asString() : null);
                        place.setName(placeNode.has("name") ? placeNode.get("name").asString() : null);
                        place.setAddress(placeNode.has("address") ? placeNode.get("address").asString() : null);
                        place.setLatitude(placeNode.has("latitude") ? placeNode.get("latitude").asDouble() : null);
                        place.setLongitude(placeNode.has("longitude") ? placeNode.get("longitude").asDouble() : null);
                        place.setPhone(placeNode.has("phone") ? placeNode.get("phone").asString() : null);
                        place.setWebsite(placeNode.has("website") ? placeNode.get("website").asString() : null);
                        place.setUrl(placeNode.has("url") ? placeNode.get("url").asString() : null);
                        place.setDistance(placeNode.has("distance") ? placeNode.get("distance").asDouble() : null);

                        if (placeNode.has("categories") && placeNode.get("categories").isArray()) {
                            List<String> categories = new ArrayList<>();
                            for (JsonNode catNode : placeNode.get("categories")) {
                                categories.add(catNode.asString());
                            }
                            place.setCategories(categories);
                        }

                        Long recommendationCount = recommendationMap.getOrDefault(placeId, 0L);

                        place.setRecommendationCount(recommendationCount);

                        place.setRecommended(recommendationCount > 0);

                        places.add(place);
                    }
                }
                return new ProductSearchResult(places, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling Node.js service: " + e.getMessage());
        }

        return new ProductSearchResult(new ArrayList<>(), null);
    }
}
