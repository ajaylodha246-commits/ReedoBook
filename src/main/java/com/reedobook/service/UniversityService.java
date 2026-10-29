package com.reedobook.service;

import com.reedobook.entity.University;
import com.reedobook.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
    
    public Optional<University> getUniversityById(Long id) {
        return universityRepository.findById(id);
    }
}
