package org.piteam.sa_backend_core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "doit être ≥ 0")
    @DecimalMax(value = "24.0", inclusive = true, message = "doit être ≤ 24")
    private Float sleepHours;

    private List<String> availability;

    @Min(value = 0, message = "doit être ≥ 0")
    @Max(value = 100, message = "doit être ≤ 100")
    private Integer baselineEnergy;
}
