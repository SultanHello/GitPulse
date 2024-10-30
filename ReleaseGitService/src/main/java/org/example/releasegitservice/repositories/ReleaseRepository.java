package org.example.releasegitservice.repositories;


import org.example.releasegitservice.models.Release;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReleaseRepository extends MongoRepository<Release,Long> {


}
