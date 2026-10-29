package com.reedobook.controller;

import com.reedobook.service.CourseService;
import com.reedobook.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        try {
            if (course.getId() != null) {
                return ResponseEntity.badRequest().body("Course ID should not be provided for a new course; it is auto-generated.");
            }
            Course savedCourse = courseService.saveCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding course: " + e.getMessage());
        }
    }

    @GetMapping("/getCourseById/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable("courseId") Long courseId) {
        try {
            Optional<Course> course = courseService.getCourseById(courseId);
            if (course.isPresent()) {
                return ResponseEntity.ok(course.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with ID: " + courseId);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching course by ID: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable("courseId") Long courseId, @RequestBody Course courseDetails) {
        try {
            Course updatedCourse = courseService.updateCourse(courseId, courseDetails);
            if (updatedCourse != null) {
                return ResponseEntity.ok(updatedCourse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with ID: " + courseId);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating course: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable("courseId") Long courseId) {
        try {
            boolean deleted = courseService.deleteCourse(courseId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with ID: " + courseId);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting course: " + e.getMessage());
        }
    }

    // NEW API: Get courses by university id
    @GetMapping("/getCourseByUniversityId/{universityId}")
    public ResponseEntity<?> getCourseByUniversityId(@PathVariable Long universityId) {
        try {
            List<Course> courses = courseService.getCourseByUniversityId(universityId);
            if (courses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching courses by university: " + e.getMessage());
        }
    }
}
