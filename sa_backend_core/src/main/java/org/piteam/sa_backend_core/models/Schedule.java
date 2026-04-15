package org.piteam.sa_backend_core.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "schedules")
public class Schedule {
    @Id
    private String id;
    private String studentId;
    private LocalDateTime generatedAt;
    private List<PlannedTask> plannedTasks;

    @Data
    public static class PlannedTask {
        private String taskId;
        private String startTime;
        private String endTime;
    }
}