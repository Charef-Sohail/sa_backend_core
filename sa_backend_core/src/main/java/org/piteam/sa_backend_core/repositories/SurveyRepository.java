package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {
    // Cette méthode nous permettra de vérifier si l'étudiant a déjà rempli un survey
    Optional<Survey> findByStudentId(String studentId);
}