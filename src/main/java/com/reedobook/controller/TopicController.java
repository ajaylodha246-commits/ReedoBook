package com.reedobook.controller;

import com.reedobook.entity.Topic;
import com.reedobook.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    // ✅ Get topics by university ID
    @GetMapping("/university/{universityId}")
    public ResponseEntity<?> getTopicsByUniversity(@PathVariable Long universityId) {
        try {
            List<Topic> topics = topicService.getTopicsByUniversityId(universityId);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching topics by university: " + e.getMessage());
        }
    }

    // ✅ Get topics by subject ID
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<?> getTopicsBySubject(@PathVariable Long subjectId) {
        try {
            List<Topic> topics = topicService.getTopicsBySubjectId(subjectId);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching topics by subject: " + e.getMessage());
        }
    }

    // ✅ Save multiple topics
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/save/batch")
    public ResponseEntity<?> saveTopics(@RequestBody List<Topic> topics) {
        try {
            List<Topic> savedTopics = topicService.saveTopics(topics);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTopics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving topics: " + e.getMessage());
        }
    }

    // ✅ Get topic by ID
    @GetMapping("/{topicId}")
    public ResponseEntity<?> getTopicById(@PathVariable Long topicId) {
        try {
            Topic topic = topicService.getTopicById(topicId);
            if (topic == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic not found with ID: " + topicId);
            }
            return ResponseEntity.ok(topic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching topic: " + e.getMessage());
        }
    }

    // ✅ Update a topic by ID
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{topicId}")
    public ResponseEntity<?> updateTopic(@PathVariable Long topicId, @RequestBody Topic topicDetails) {
        try {
            Topic updatedTopic = topicService.updateTopic(topicId, topicDetails);
            if (updatedTopic == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic not found with ID: " + topicId);
            }
            return ResponseEntity.ok(updatedTopic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating topic: " + e.getMessage());
        }
    }

    // ✅ Add Topic by Subject ID and Topic Name
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/add/single/{subjectId}")
    public ResponseEntity<?> addTopicBySubjectAndName(@PathVariable Long subjectId, @RequestBody Topic topic) {
        try {
            if (topic.getTopic_id() != null) {
                return ResponseEntity.badRequest().body("Topic ID should not be provided for a new topic; it is auto-generated.");
            }
            topic.setSubject_id(subjectId);
            if (topic.getTopic_name() == null || topic.getTopic_name().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Topic name cannot be empty.");
            }
            Topic savedTopic = topicService.saveTopic(topic);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTopic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding topic: " + e.getMessage());
        }
    }

    // ✅ Upload audio file (.mp3, .webm, .wav, .ogg) and update Topic with unique filename
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping(value = "/upload/audio/{topicId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAudioFile(
            @PathVariable Long topicId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("topic") Topic topicDetails) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResponseEntity.badRequest().body("File must have a valid name.");
            }

            // Allowed audio extensions
            String lowerFileName = originalFilename.toLowerCase();
            if (!(lowerFileName.endsWith(".mp3") ||
                    lowerFileName.endsWith(".webm") ||
                    lowerFileName.endsWith(".wav") ||
                    lowerFileName.endsWith(".ogg"))) {
                return ResponseEntity.badRequest()
                        .body("Only audio files with extensions .mp3, .webm, .wav, or .ogg are allowed.");
            }

            // Extract file extension
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Upload directory
            String uploadDir = System.getProperty("user.dir") + "/uploads/audio_files/";
            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Generate unique file name with timestamp + extension
            String uniqueFileName = System.currentTimeMillis() + fileExtension;

            // Save file on server
            File destFile = new File(uploadDir + uniqueFileName);
            file.transferTo(destFile);

            // Get existing topic from DB
            Topic existingTopic = topicService.getTopicById(topicId);
            if (existingTopic == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic not found with ID: " + topicId);
            }

            // Update topic fields
            existingTopic.setTopic_name(topicDetails.getTopic_name());
            existingTopic.setEnglish_column(topicDetails.getEnglish_column());
            existingTopic.setLocal_column(topicDetails.getLocal_column());
            existingTopic.setUniversity_id(topicDetails.getUniversity_id());
            existingTopic.setSubject_id(topicDetails.getSubject_id());
            existingTopic.setUnit(topicDetails.getUnit());

            // Set audio file relative path for frontend access
            existingTopic.setAudio_file("/audio_files/" + uniqueFileName);

            // Update topic in database
            topicService.updateTopic(topicId, existingTopic);

            return ResponseEntity.ok("Audio file and topic uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("I/O Error while saving the file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file and topic: " + e.getMessage());
        }
    }
}

