package org.example.releasegitservice.repositories;


import org.example.releasegitservice.models.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release,Long> {


}
