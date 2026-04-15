package org.piteam.sa_backend_core.dto.planner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentProfileDTO {

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("wake_up_time")
    private String wakeUpTime;

    @JsonProperty("sleep_time")
    private String sleepTime;

    @JsonProperty("peak_productivity")
    private String peakProductivity;

    @JsonProperty("energy_score")
    private int energyScore;
}