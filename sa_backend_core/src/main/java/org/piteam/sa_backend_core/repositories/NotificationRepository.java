package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    // Utile pour afficher les notifications non lues à l'étudiant
    List<Notification> findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(String studentId);
}