// File: src/main/java/com/reedobook/entity/Topic.java
package com.reedobook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "topic_details")
public class Topic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topic_id; // Changed to snake_case

    @Column(name = "topic_name", nullable = false, length = 255)
    private String topic_name; // Changed to snake_case

    @Column(name = "english_column", columnDefinition = "TEXT")
    private String english_column; // Changed to snake_case

    @Column(name = "local_column", columnDefinition = "TEXT")
    private String local_column; // Changed to snake_case

    @Column(name = "audio_file", length = 500)
    private String audio_file; // Changed to snake_case

    @Column(name = "university_id", nullable = false)
    private Long university_id; // Changed to snake_case

    @Column(name = "subject_id", nullable = false)
    private Long subject_id; // Changed to snake_case

    private String unit;

    // --- Constructors ---
    public Topic() {
    }

    public Topic(String topic_name, String english_column, String local_column, String audio_file, Long university_id, Long subject_id, String unit) {
        this.topic_name = topic_name;
        this.english_column = english_column;
        this.local_column = local_column;
        this.audio_file = audio_file;
        this.university_id = university_id;
        this.subject_id = subject_id;
        this.unit = unit;
    }

    // --- Getters and Setters (Updated to snake_case) ---

    public Long getTopic_id() { // Updated getter
        return topic_id;
    }

    public void setTopic_id(Long topic_id) { // Updated setter
        this.topic_id = topic_id;
    }

    public String getTopic_name() { // Updated getter
        return topic_name;
    }

    public void setTopic_name(String topic_name) { // Updated setter
        this.topic_name = topic_name;
    }

    public String getEnglish_column() {
        return english_column;
    }

    public void setEnglish_column(String english_column) {
        this.english_column = english_column;
    }

    public String getLocal_column() {
        return local_column;
    }

    public void setLocal_column(String local_column) {
        this.local_column = local_column;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public Long getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(Long university_id) {
        this.university_id = university_id;
    }

    public Long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topic_id=" + topic_id +
                ", topic_name='" + topic_name + '\'' +
                ", english_column='" + english_column + '\'' +
                ", local_column='" + local_column + '\'' +
                ", audio_file='" + audio_file + '\'' +
                ", university_id=" + university_id +
                ", subject_id=" + subject_id +
                ", unit=" + unit +
                '}';
    }
}