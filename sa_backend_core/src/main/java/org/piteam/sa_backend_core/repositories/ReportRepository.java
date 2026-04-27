package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    // Pour l'étudiant : voir l'historique de ses propres signalements
    List<Report> findByStudentIdOrderByCreatedAtDesc(String studentId);

    // Pour l'admin : trier pour voir les plus récents en premier
    List<Report> findAllByOrderByCreatedAtDesc();
}