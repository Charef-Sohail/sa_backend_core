package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task,String> {
}
