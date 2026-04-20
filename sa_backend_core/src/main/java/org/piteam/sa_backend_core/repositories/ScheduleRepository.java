package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByStudentId(String studentId);
    List<Schedule> findByTaskId(String taskId);
    List<Schedule> findByStudentIdAndTaskId(String studentId, String taskId);
    List<Schedule> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    void deleteByTaskId(String taskId);
    // Détection de chevauchement (règle métier critique)
    @Query("{ 'studentId': ?0, 'startTime': { $lt: ?2 }, 'endTime': { $gt: ?1 } }")
    List<Schedule> findOverlappingSchedules(String studentId, LocalDateTime start, LocalDateTime end);

    // Ne pas déppaser le durée totale de la tâche
    @Query(value = "{ 'taskId': ?0 }", fields = "{ 'startTime': 1, 'endTime': 1 }")
    List<Schedule> findSchedulesWithTimesByTaskId(String taskId);

     // Optionnel : Supprime l'ancien planning avant d'en générer un nouveau
    void deleteByStudentId(String studentId);
}


