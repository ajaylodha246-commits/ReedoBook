package com.reedobook.service;

import com.reedobook.entity.College;
import com.reedobook.entity.University;
import com.reedobook.repository.CollegeRepository;
import com.reedobook.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository, UniversityRepository universityRepository) {
        this.collegeRepository = collegeRepository;
        this.universityRepository = universityRepository;
    }

    /**
     * Saves a college entity after verifying the associated university exists.
     *
     * @param college College entity to be saved; must contain university with valid ID.
     * @return The saved College entity.
     * @throws IllegalArgumentException if university information is missing.
     * @throws RuntimeException if university with given ID does not exist.
     */
    public College saveCollege(College college) {
        if (college == null || college.getUniversity() == null || college.getUniversity().getId() == null) {
            throw new IllegalArgumentException("University information (with valid ID) is required to save a college.");
        }

        Long universityId = college.getUniversity().getId();

        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University not found with ID: " + universityId));

        // Associate managed university entity to college before saving
        college.setUniversity(university);

        return collegeRepository.save(college);
    }

    /**
     * Retrieves a list of colleges that belong to the specified university name.
     *
     * @param universityId The name of the university.
     * @return List of College entities associated with the university.
     *         Empty list if none found.
     */
    public List<College> getCollegesByUniversityName(Long universityId) {
        if (universityId==0) {
            throw new IllegalArgumentException("University name must be provided.");
        }
        return collegeRepository.findCollegeByUniversityId(universityId);
    }
    
    public Optional<College> getCollegeById(Long collegeId) {
        return collegeRepository.findById(collegeId);
    }
}
