package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Faq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FaqRepository extends MongoRepository<Faq, String> {
    List<Faq> findByCategory(String category);

    @Query("{ $or: [ { 'question': { $regex: ?0, $options: 'i' } }, { 'answer': { $regex: ?0, $options: 'i' } } ] }")
    List<Faq> searchByKeyword(String keyword);
    List<Faq> findByAnswerIsNotNull();
}