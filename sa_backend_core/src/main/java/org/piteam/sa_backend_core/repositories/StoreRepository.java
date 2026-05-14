package org.piteam.sa_backend_core.repositories;

import org.piteam.sa_backend_core.models.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreRepository extends MongoRepository<Store,String> {
    Optional<Store> findByPlaceId(String placeId);
}
