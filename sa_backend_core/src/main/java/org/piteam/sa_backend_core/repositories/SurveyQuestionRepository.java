package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.SurveyQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {

    // Récupère toutes les questions triées par ordre d'affichage
    List<SurveyQuestion> findAllByOrderByDisplayOrderAsc();
}