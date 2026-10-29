package com.reedobook.service;

import com.reedobook.entity.Branch;
import com.reedobook.entity.Course;
import com.reedobook.repository.BranchRepository;
import com.reedobook.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final CourseRepository courseRepository;

    public BranchService(BranchRepository branchRepository,
                         CourseRepository courseRepository) {
        this.branchRepository = branchRepository;
        this.courseRepository = courseRepository;
    }

    // Create Branch
    public Branch createBranch(Long courseId, Branch branch) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        branch.setCourse(course);

        return branchRepository.save(branch);
    }

    // Get all branches
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    // Get branch by ID
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    // Update branch
    public Branch updateBranch(Long id, Branch updatedBranch) {

        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        existingBranch.setBranchName(updatedBranch.getBranchName());

        return branchRepository.save(existingBranch);
    }

    // Delete branch
    public void deleteBranch(Long id) {

        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        branchRepository.delete(branch);
    }
}