package org.piteam.sa_backend_core.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProfileResponse {
    private String id;
    private String studentId;
    private Float sleepHours;
    private List<String> availability;
    private Integer baselineEnergy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean healthySleepPattern; // Champ calculé
}
