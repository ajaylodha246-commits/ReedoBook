package com.reedobook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "university_id", nullable = false)
    private Long universityId;

    // Default constructor
    public Course() {
    }

    // Constructor without ID
    public Course(String name, Long universityId) {
        this.name = name;
        this.universityId = universityId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", universityId=" + universityId +
                '}';
    }
}
