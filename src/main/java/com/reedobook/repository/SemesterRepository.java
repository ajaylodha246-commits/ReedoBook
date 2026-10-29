// File: src/main/java/com/reedobook/SemesterRepository.java
package com.reedobook.repository;

import com.reedobook.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Recommended annotation for clarity

import java.util.List;

@Repository // Marks this interface as a Spring Data JPA repository component
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByCourseId(Long courseId);
}