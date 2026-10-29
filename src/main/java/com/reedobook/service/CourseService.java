package com.reedobook.service;

import com.reedobook.entity.Course;
import com.reedobook.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Save a new course, verifying that the associated college exists.
     */
    public Course saveCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        return courseRepository.save(course);
    }

    /**
     * Get a course by its ID.
     */
    public Optional<Course> getCourseById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return courseRepository.findById(id);
    }

    /**
     * Update an existing course with new details.
     */
    public Course updateCourse(Long courseId, Course courseDetails) {
        if (courseId == null || courseDetails == null) {
            throw new IllegalArgumentException("Course ID and course details must not be null");
        }

        return courseRepository.findById(courseId)
                .map(existingCourse -> {
                    existingCourse.setName(courseDetails.getName());
                    existingCourse.setUniversityId(courseDetails.getUniversityId());
                    return courseRepository.save(existingCourse);
                }).orElse(null);
    }

    /**
     * Delete a course by ID.
     */
    public boolean deleteCourse(Long courseId) {
        if (courseId == null) {
            return false;
        }
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return true;
        }
        return false;
    }

    /**
     * Find courses by university ID.
     */
    public List<Course> getCourseByUniversityId(Long universityId) {
        if (universityId == null) {
            return List.of();
        }
        return courseRepository.findByUniversityId(universityId);
    }
}
