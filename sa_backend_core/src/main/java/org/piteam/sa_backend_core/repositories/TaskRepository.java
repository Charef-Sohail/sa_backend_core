package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByStudentId(String studentId);
    List<Task> findByStatus(String status);
    List<Task> findByCategory(String category);

    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'title': 1 }")
    List<Task> findTitleAndStudentByIdIn(Collection<String> ids);
}
