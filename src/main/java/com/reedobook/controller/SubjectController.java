package com.reedobook.controller;

import com.reedobook.service.SubjectService;
import com.reedobook.entity.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // ✅ Save single subject (POST)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addSubject")  //	Add a single subject
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
        try {
            Subject savedSubject = subjectService.saveSubject(subject);
            return ResponseEntity.ok(savedSubject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving subject: " + e.getMessage());
        }
    }

    // ✅ Get subjects by semester ID (GET)
    @GetMapping("/getSubjectBySemesterId/{semesterId}")
    public ResponseEntity<?> getSubjectBySemesterId(@PathVariable Long semesterId) {
        try {
            List<Subject> subjects = subjectService.getSubjectsBySemesterId(semesterId);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching subjects: " + e.getMessage());
        }
    }
}
