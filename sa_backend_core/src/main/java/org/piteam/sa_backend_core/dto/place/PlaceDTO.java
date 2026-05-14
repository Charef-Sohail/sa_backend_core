package org.piteam.sa_backend_core.dto.place;

import lombok.Data;

import java.util.List;

@Data
public class PlaceDTO {
    private String id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private List<String> categories;
    private String url;
    private Double distance; // en km
    private Long recommendationCount = 0L;
    private boolean recommended = false;
}