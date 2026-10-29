package com.reedobook.service;

import com.reedobook.entity.Subject;
import com.reedobook.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // ✅ Save a single subject
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // ✅ Save multiple subjects at once
    public List<Subject> saveSubjects(List<Subject> subjects) {
        return subjectRepository.saveAll(subjects);
    }

    // ✅ Get subjects by semester id
    public List<Subject> getSubjectsBySemesterId(Long semesterId) {
        return subjectRepository.findBySemesterId(semesterId);
    }
}
