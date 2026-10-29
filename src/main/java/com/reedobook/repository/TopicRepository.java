// File: src/main/java/com/reedobook/repository/TopicRepository.java
package com.reedobook.repository;

import com.reedobook.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Make sure this import is here
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    // --- CRUCIAL CHANGE HERE ---
    // Explicitly define the query for findByUniversity_id
    // JPQL uses the entity's field names (e.g., university_id), not the database column names directly.
    @Query("SELECT t FROM Topic t WHERE t.university_id = :universityId")
    List<Topic> findByUniversity_id(@Param("universityId") Long universityId);


    // This was previously updated and is now confirmed correct
    @Query("SELECT t FROM Topic t WHERE t.subject_id = :subjectId")
    List<Topic> findBySubject_id(@Param("subjectId") Long subjectId);

    // JpaRepository automatically provides methods like:
    // - save(Topic entity)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - existsById(Long id)
}