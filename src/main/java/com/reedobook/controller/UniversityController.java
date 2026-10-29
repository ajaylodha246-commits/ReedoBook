package com.reedobook.controller;

import com.reedobook.entity.University;
import com.reedobook.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@EnableMethodSecurity
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/university")  // changed to include /api prefix
public class UniversityController {

    private static final Logger logger = Logger.getLogger(UniversityController.class.getName());

    @Autowired
    private UniversityService universityService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addUniversity")
    public ResponseEntity<?> addUniversity(@RequestBody University university) {
        try {
            University saved = universityService.saveUniversity(university);
            logger.info("University saved: " + saved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);  // 201 Created
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving university", e);
            return ResponseEntity.badRequest().body("Error saving university: " + e.getMessage());
        }
    }

    @GetMapping("/getAllUniversity")
    public ResponseEntity<?> getAllUniversity() {
        try {
            List<University> universities = universityService.getAllUniversities();
            logger.info("Fetched " + universities.size() + " universities.");
            return ResponseEntity.ok(universities);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching universities", e);
            return ResponseEntity.badRequest().body("Error fetching universities: " + e.getMessage());
        }
    }
}
