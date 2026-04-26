package org.piteam.sa_backend_core.services;

import org.piteam.sa_backend_core.models.Notification;
import org.piteam.sa_backend_core.models.Schedule;
import org.piteam.sa_backend_core.repositories.NotificationRepository;
import org.piteam.sa_backend_core.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Scheduled(fixedRate = 60000)
    public void checkUpcomingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in15Minutes = now.plusMinutes(15);

        // 1. On récupère TOUTES les tâches qui n'ont pas encore déclenché d'alerte
        List<Schedule> unnotifiedTasks = scheduleRepository.findUnnotifiedTasks();
        int alertCount = 0;

        for (Schedule schedule : unnotifiedTasks) {
            if (schedule.getStartTime() == null) continue;

            // MAGIE : C'est déjà un LocalDateTime, pas besoin de le convertir !
            LocalDateTime taskTime = schedule.getStartTime();

            // 2. C'est Java qui vérifie si c'est dans les 15 prochaines minutes
            if (taskTime.isAfter(now) && taskTime.isBefore(in15Minutes)) {

                // --- ON CRÉE LA NOTIFICATION ---
                Notification notif = new Notification(
                        schedule.getStudentId(),
                        "Rappel de tâche imminente",
                        "Préparez-vous ! Une tâche commence bientôt."
                );
                notificationRepository.save(notif);

                // --- ON BLOQUE LES SPAMS ---
                schedule.setNotified(true);
                scheduleRepository.save(schedule);

                alertCount++;
            }
        }

        if (alertCount > 0) {
            System.out.println("🔔 BINGO ! " + alertCount + " alerte(s) générée(s) et sauvegardée(s) dans MongoDB !");
        }
    }

}