package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    /**
     * Trouve le dernier planning généré pour un étudiant spécifique.
     * Spring Data comprend "findBy" + "StudentId" et crée la requête automatiquement.
     */
    Optional<Schedule> findByStudentId(String studentId);

    /**
     * Optionnel : Supprime l'ancien planning avant d'en générer un nouveau
     */
    void deleteByStudentId(String studentId);
}