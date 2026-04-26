package org.piteam.sa_backend_core.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schedules")
public class Schedule {
    @Id
    private String id;
    private String studentId;
    private String taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float score;
    private String source;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Permet de savoir si on a déjà envoyé une alerte pour ce créneau
    private boolean notified = false;

    public long getDurationMinutes() {
        return startTime != null && endTime != null
                ? java.time.Duration.between(startTime, endTime).toMinutes()
                : 0;
    }

    @AssertTrue(message = "endTime doit être après startTime")
    public boolean isTimeRangeValid() {
        return startTime == null || endTime == null || endTime.isAfter(startTime);
    }
}
