package org.piteam.sa_backend_core.dto.place;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductSearchResult {
    List<PlaceDTO> places;
    String price;
}
