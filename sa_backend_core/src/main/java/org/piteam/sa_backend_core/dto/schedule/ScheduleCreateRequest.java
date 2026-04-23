package org.piteam.sa_backend_core.dto.schedule;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleCreateRequest {
    @NotBlank(message = "studentId est requis")
    private String studentId;

    @NotBlank(message = "taskId est requis")
    private String taskId;

    @NotNull(message = "startTime est requis")
    @Future(message = "StartTime doit être dans le futur")
    private LocalDateTime startTime;

    @NotNull(message = "endTime est requis")
    @Future(message = "EndTime doit être dans le futur")
    private LocalDateTime endTime;

    @Min(value = 0) @Max(value = 100)
    private Float score;

    //@Pattern(regexp = "MANUAL|AUTO_GENERATED|IMPORTED|SYNC_EXTERNAL")
    private String source;

}
