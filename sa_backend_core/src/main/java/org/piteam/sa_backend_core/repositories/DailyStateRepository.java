package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.DailyState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyStateRepository extends MongoRepository<DailyState, String> {
    // Trouve l'état d'un étudiant spécifique pour une date précise
    Optional<DailyState> findByStudentIdAndDate(String studentId, LocalDate date);

}