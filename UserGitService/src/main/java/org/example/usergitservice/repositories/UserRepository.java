package org.example.usergitservice.repositories;

import org.example.usergitservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByGitUsername(String gitUsername);
}
