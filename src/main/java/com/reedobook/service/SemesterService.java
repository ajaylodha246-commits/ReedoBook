// File: src/main/java/com/reedobook/SemesterService.java
package com.reedobook.service;

import com.reedobook.entity.Semester;
import com.reedobook.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Import Optional for methods that might not find a result

@Service // Marks this class as a Spring service component
public class SemesterService {

    private final SemesterRepository semesterRepository; // Use 'final' and constructor injection

    @Autowired // Spring will automatically inject an instance of SemesterRepository
    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    /**
     * Saves a new Semester to the database.
     * @param semester The Semester object to save. Its ID should be null for new creations.
     * @return The saved Semester object, including its database-generated ID.
     */
    public Semester saveSemester(Semester semester) {
        // You can add business logic or validation here before saving
        // For example, checking if the associated Course ID exists (though database foreign key handles this too)
        return semesterRepository.save(semester);
    }

    public List<Semester> getSemesterByCourseId(Long courseId) {
        return semesterRepository.findByCourseId(courseId);
    }

    /**
     * Retrieves a single semester by its ID.
     * @param id The ID of the semester to retrieve.
     * @return An Optional containing the Semester object if found, or an empty Optional if not.
     */
    public Optional<Semester> getSemesterById(Long id) {
        return semesterRepository.findById(id);
    }

    /**
     * Updates an existing semester.
     * @param semesterId The ID of the semester to update.
     * @param semesterDetails A Semester object containing the updated information.
     * @return The updated Semester object if found and successfully updated, otherwise null.
     */
    public Semester updateSemester(Long semesterId, Semester semesterDetails) {
        // First, try to find the existing semester in the database
        Optional<Semester> existingSemesterOptional = semesterRepository.findById(semesterId);

        if (existingSemesterOptional.isPresent()) {
            Semester existingSemester = existingSemesterOptional.get();

            // Update only the fields that are allowed to be modified
            // You are strictly keeping semester_name and course_id
            existingSemester.setName(semesterDetails.getName());
            existingSemester.setCourseId(semesterDetails.getCourseId()); // Ensure this course_id exists!

            // Save the modified existingSemester back to the database
            return semesterRepository.save(existingSemester);
        } else {
            // If no semester found with the given ID, return null
            return null;
        }
    }

    /**
     * Deletes a semester by its ID.
     * @param semesterId The ID of the semester to delete.
     * @return true if the semester was found and deleted, false otherwise.
     */
    public boolean deleteSemester(Long semesterId) {
        // Check if the semester exists before attempting the deletion
        if (semesterRepository.existsById(semesterId)) {
            semesterRepository.deleteById(semesterId); // Perform the deletion
            return true; // Deletion successful
        }
        return false; // Semester not found
    }
}