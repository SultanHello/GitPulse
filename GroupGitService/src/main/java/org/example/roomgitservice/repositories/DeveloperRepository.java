package org.example.roomgitservice.repositories;

import org.example.roomgitservice.models.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer,Long> {

}
