package org.piteam.sa_backend_core.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleUpdateRequest {
    @Future(message = "StartTime doit être dans le futur")
    private LocalDateTime startTime;
    @Future(message = "EndTime doit être dans le futur")
    private LocalDateTime endTime;

    @Min(value = 0) @Max(value = 100)
    private Float score;

    //@Pattern(regexp = "MANUAL|AUTO_GENERATED|IMPORTED|SYNC_EXTERNAL")
    private String source;

}
