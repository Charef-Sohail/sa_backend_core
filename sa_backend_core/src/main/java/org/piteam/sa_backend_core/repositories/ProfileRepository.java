package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {
    boolean existsByStudentId(String studentId);
    Optional<Profile> findByStudentId(String studentId);
    void deleteByStudentId(String studentId);
}
