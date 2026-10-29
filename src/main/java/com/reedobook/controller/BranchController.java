package com.reedobook.controller;

import com.reedobook.entity.Branch;
import com.reedobook.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

          // This Class Created  by Ajay

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    // Create Branch
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Branch> createBranch(
            @PathVariable Long courseId,
            @RequestBody Branch branch) {

        Branch savedBranch = branchService.createBranch(courseId, branch);

        return ResponseEntity.ok(savedBranch);
    }

    // Get all branches
    @GetMapping("/getAll")
    public ResponseEntity<List<Branch>> getAllBranches() {

        return ResponseEntity.ok(
                branchService.getAllBranches()
        );
    }

    // Get branch by ID
    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                branchService.getBranchById(id)
        );
    }

    // Update branch
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(
            @PathVariable Long id,
            @RequestBody Branch branch) {

        return ResponseEntity.ok(
                branchService.updateBranch(id, branch)
        );
    }

    // Delete branch
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(
            @PathVariable Long id) {

        branchService.deleteBranch(id);

        return ResponseEntity.ok("Branch deleted successfully");
    }
}