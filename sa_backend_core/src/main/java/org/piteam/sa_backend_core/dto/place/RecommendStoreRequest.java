package org.piteam.sa_backend_core.dto.place;

import lombok.Data;

import java.util.List;

@Data
public class RecommendStoreRequest {
    //private String userId;
    private String product;
    private String placeId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private String url;
    private List<String> categories;
}