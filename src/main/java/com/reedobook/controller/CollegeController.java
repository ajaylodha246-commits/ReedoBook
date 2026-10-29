package com.reedobook.controller;

import com.reedobook.entity.College;
import com.reedobook.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:3000")  // Enable CORS for React frontend
@RestController
@RequestMapping("/college")
public class CollegeController {

    private static final Logger logger = Logger.getLogger(CollegeController.class.getName());

    @Autowired
    private CollegeService collegeService;

    /**
     * POST: Add a new college associated with a university.
     * Request body must contain college name and university (with valid ID).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCollege")
    public ResponseEntity<?> addCollege(@RequestBody College college) {
        try {
            College saved = collegeService.saveCollege(college);
            logger.info("College saved: " + saved.getName() +
                    " under university: " + (saved.getUniversity() != null ? saved.getUniversity().getName() : "null"));
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving college", e);
            return ResponseEntity.badRequest().body("Error saving college: " + e.getMessage());
        }
    }

    /**
     * GET: Retrieve all colleges under a given university name.
     */
    @GetMapping("/getCollegeByUniversityId/{universityId}")
    public ResponseEntity<?> getCollegeByUniversity(@PathVariable Long universityId) {
        try {
            List<College> colleges = collegeService.getCollegesByUniversityName(universityId);
            logger.info("Fetched " + colleges.size() + " colleges for university: " + universityId);
            return ResponseEntity.ok(colleges);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching colleges for university: " + universityId, e);
            return ResponseEntity.badRequest().body("Error fetching colleges: " + e.getMessage());
        }
    }
}
