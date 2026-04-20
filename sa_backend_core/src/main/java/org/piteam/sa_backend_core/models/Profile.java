package org.piteam.sa_backend_core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private String id;
    private String studentId;

    private Float sleepHours;

    @Builder.Default
    private List<String> availability = new ArrayList<>(); // ex: ["MONDAY_09-12", "WEDNESDAY_14-17"]
    private Integer baselineEnergy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // === Méthodes métier ===
    public boolean isHealthySleepPattern() {
        return sleepHours != null && sleepHours >= 7.0 && sleepHours <= 9.0;
    }
}
