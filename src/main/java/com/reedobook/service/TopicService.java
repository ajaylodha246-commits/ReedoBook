// File: src/main/java/com/reedobook/service/TopicService.java
package com.reedobook.service;

import com.reedobook.repository.TopicRepository;
import com.reedobook.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * Retrieves all topics from the database.
     *
     * @return A list of all Topic objects. Can be empty if no topics exist.
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * Retrieves all topics associated with a specific university ID.
     *
     * @param universityId The ID of the university.
     * @return A list of Topic objects linked to the given university ID.
     */
    public List<Topic> getTopicsByUniversityId(Long universityId) {
        // This requires a method in your TopicRepository: List<Topic> findByUniversity_id(Long universityId);
        return topicRepository.findByUniversity_id(universityId);
    }

    /**
     * Retrieves all topics associated with a specific subject ID.
     *
     * @param subjectId The ID of the subject.
     * @return A list of Topic objects linked to the given subject ID.
     */
    public List<Topic> getTopicsBySubjectId(Long subjectId) {
        // This requires a method in your TopicRepository: List<Topic> findBySubject_id(Long subjectId);
        return topicRepository.findBySubject_id(subjectId);
    }

    /**
     * Saves a single new Topic to the database.
     *
     * @param topic The Topic object to save. Its ID should be null for new creations.
     * @return The saved Topic object, including its database-generated ID.
     */
    public Topic saveTopic(Topic topic) {
        // You can add business logic here before saving,
        // e.g., fetching universityId based on subjectId if not provided in the topic object
        // if (topic.getUniversity_id() == null && topic.getSubject_id() != null) {
        //     // Logic to determine university_id from subject_id
        //     // e.g., Long uniId = subjectService.getUniversityIdBySubjectId(topic.getSubject_id());
        //     // topic.setUniversity_id(uniId);
        // }
        return topicRepository.save(topic);
    }

    /**
     * Saves multiple new Topics to the database in a batch.
     *
     * @param topics A list of Topic objects to save. Their IDs should be null for new creations.
     * @return A list of the saved Topic objects, including their database-generated IDs.
     */
    public List<Topic> saveTopics(List<Topic> topics) {
        return topicRepository.saveAll(topics);
    }

    /**
     * Retrieves a single topic by its ID.
     *
     * @param topicId The ID of the topic to retrieve.
     * @return The Topic object if found, otherwise null.
     */
    public Topic getTopicById(Long topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        return topicOptional.orElse(null);
    }

    /**
     * Updates an existing topic.
     * @param topicId The ID of the topic to update.
     * @param topicDetails A Topic object containing the updated information.
     * @return The updated Topic object if found and successfully updated, otherwise null.
     */
    public Topic updateTopic(Long topicId, Topic topicDetails) {
        // First, try to find the existing topic in the database
        Optional<Topic> existingTopicOptional = topicRepository.findById(topicId);

        if (existingTopicOptional.isPresent()) {
            Topic existingTopic = existingTopicOptional.get();

            // Update the fields of the existing topic with the new details
            // IMPORTANT: Using the correct getter/setter names (snake_case)
            existingTopic.setTopic_name(topicDetails.getTopic_name());
            existingTopic.setEnglish_column(topicDetails.getEnglish_column());
            existingTopic.setLocal_column(topicDetails.getLocal_column());
            existingTopic.setAudio_file(topicDetails.getAudio_file());

            // Ensure these foreign keys are handled correctly.
            // If they are mandatory and not provided in topicDetails,
            // you might want to fetch them from the existingTopic or handle the error.
            existingTopic.setUniversity_id(topicDetails.getUniversity_id());
            existingTopic.setSubject_id(topicDetails.getSubject_id());
            existingTopic.setUnit(topicDetails.getUnit());

            // Save the modified existingTopic back to the database
            return topicRepository.save(existingTopic);
        } else {
            // If no topic found with the given ID, return null
            return null;
        }
    }

    /**
     * Deletes a topic by its ID.
     * @param topicId The ID of the topic to delete.
     * @return true if the topic was found and deleted, false otherwise.
     */
    public boolean deleteTopic(Long topicId) {
        // Check if the topic exists before attempting deletion
        if (topicRepository.existsById(topicId)) {
            topicRepository.deleteById(topicId); // Perform the deletion
            return true; // Deletion successful
        }
        return false; // Topic not found
    }
}