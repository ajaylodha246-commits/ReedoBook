
package com.reedobook.service;

import com.reedobook.entity.Content;
import com.reedobook.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ContentService {

    @Autowired
    private  FileStorageService fileStorageService;

    private final ContentRepository contentRepo;

    public ContentService(ContentRepository contentRepo) {
        this.contentRepo = contentRepo;
    }

    // Save new content
    public Content saveContent(
            Content content,
            MultipartFile image) throws IOException {

        if (image != null && !image.isEmpty()) {
            String imagePath =
                    fileStorageService.saveImage(image);

            content.setImageUrl(imagePath);
        }

        return contentRepo.save(content);
    }

    // Get content by ID
    public Content getContentById(Long id) {
        return contentRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Content not found with ID: " + id));
    }

    // Get all content
    public List<Content> getAllContent() {
        return contentRepo.findAll();
    }
}
