package com.reedobook.repository;

import com.reedobook.entity.User; // This import is correct and necessary
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // For Optional return type

public interface UserRepository extends JpaRepository<User, Long> {

    // This method allows you to find a User by their email address.
    // Spring Data JPA automatically generates the query for this based on the method name.
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}