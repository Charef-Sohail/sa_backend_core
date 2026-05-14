package org.piteam.sa_backend_core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "stores")
public class Store {
    @Id
    private String id;
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