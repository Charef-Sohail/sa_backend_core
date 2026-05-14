package org.piteam.sa_backend_core.controllers;

import org.piteam.sa_backend_core.dto.place.ApiResponse;
import org.piteam.sa_backend_core.dto.place.PlaceDTO;
import org.piteam.sa_backend_core.dto.place.PlaceSearchRequest;
import org.piteam.sa_backend_core.dto.place.ProductSearchResult;
import org.piteam.sa_backend_core.services.PlaceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@CrossOrigin(origins = "http://localhost:3000")
public class PlaceSearchController {
    @Autowired
    private PlaceSearchService placeSearchService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchPlaces(@RequestBody PlaceSearchRequest request) {
        try {
            if (request.getProduct() == null || request.getLatitude() == null || request.getLongitude() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Missing required fields: product, latitude, longitude"));
            }

            ProductSearchResult results = placeSearchService.searchNearbyPlaces(request);
            return ResponseEntity.ok(new ApiResponse(true, results));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
