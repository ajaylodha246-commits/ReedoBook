
package com.reedobook.controller;

import com.reedobook.entity.Content;
import com.reedobook.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // Get content by ID
    @GetMapping("/{id}")
    public Content getContentById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    // Add content
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping(
            value = "/addContent",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Content addContent(
            @ModelAttribute Content content,
            @RequestParam(required = false) MultipartFile image)
            throws IOException {

        return contentService.saveContent(content, image);
    }

    // Get all content
    @GetMapping("/getAll")
    public List<Content> getAllContent() {
        return contentService.getAllContent();
    }
}
