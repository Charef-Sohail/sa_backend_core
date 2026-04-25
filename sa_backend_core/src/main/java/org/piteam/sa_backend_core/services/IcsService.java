package org.piteam.sa_backend_core.services;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import org.piteam.sa_backend_core.models.Task;
import org.piteam.sa_backend_core.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class IcsService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> importIcsCalendar(MultipartFile file, String studentId) throws Exception {
        List<Task> importedTasks = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(is);

            // On parcourt chaque événement du calendrier (Version 3.x)
            calendar.getComponents(Component.VEVENT).forEach(component -> {
                VEvent event = (VEvent) component;

                // Extraction sécurisée du titre
                String title = event.getSummary() != null ? event.getSummary().getValue() : "Cours sans titre";

                // En v3, getDate() renvoie un java.util.Date qu'on convertit en LocalDateTime
                LocalDateTime start = event.getStartDate().getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();

                LocalDateTime end = event.getEndDate().getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();

                Task fixedTask = new Task();
                fixedTask.setStudentId(studentId);
                fixedTask.setTitle("[COURS] " + title);
                fixedTask.setStatus("TODO");
                fixedTask.setPriority("HIGH");
                fixedTask.setCategory("COURS");
                fixedTask.setDeadline(end);
                fixedTask.setDifficulty(1);

                long duration = java.time.Duration.between(start, end).toMinutes();
                fixedTask.setDuration((int) duration);

                List<String> tags = new ArrayList<>();
                tags.add("FIXED_EVENT");
                fixedTask.setTags(tags);

                importedTasks.add(fixedTask);
            });
        }

        return taskRepository.saveAll(importedTasks);
    }
}