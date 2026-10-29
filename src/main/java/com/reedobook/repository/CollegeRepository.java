package com.reedobook.repository;

import com.reedobook.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    List<College> findCollegeByUniversityId(Long university_id);
}
