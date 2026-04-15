package org.piteam.sa_backend_core.dto.planner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OptimizationResponseDTO {
    private String status;
    private String message;
    private List<ScheduledTaskDTO> schedule;

    @Data
    public static class ScheduledTaskDTO {
        @JsonProperty("task_id")
        private String taskId;

        @JsonProperty("start_time")
        private String startTime;

        @JsonProperty("end_time")
        private String endTime;
    }
}