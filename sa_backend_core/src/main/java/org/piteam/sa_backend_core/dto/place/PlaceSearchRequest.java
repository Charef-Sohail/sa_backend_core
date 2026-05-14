package org.piteam.sa_backend_core.dto.place;

import lombok.Data;

@Data
public class PlaceSearchRequest {
    private String product;
    private Double latitude;
    private Double longitude;
    private Integer maxResults = 1;
}
