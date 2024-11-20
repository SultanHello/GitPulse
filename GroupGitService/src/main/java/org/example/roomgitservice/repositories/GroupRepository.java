package org.example.roomgitservice.repositories;

import org.example.roomgitservice.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findById(Long id);
    boolean existsById(Long id);

}
