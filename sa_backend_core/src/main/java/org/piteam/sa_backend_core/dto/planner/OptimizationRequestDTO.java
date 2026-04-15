package org.piteam.sa_backend_core.dto.planner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OptimizationRequestDTO {
    private StudentProfileDTO student;
    private List<TaskDTO> tasks;

    @JsonProperty("start_date") // Force Jackson à utiliser le format snake_case pour Python
    private ZonedDateTime startDate;
}