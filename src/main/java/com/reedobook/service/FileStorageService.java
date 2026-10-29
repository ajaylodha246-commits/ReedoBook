
package com.reedobook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir =
            Paths.get("uploads/images").toAbsolutePath().normalize();

    public String saveImage(MultipartFile image) throws IOException {

        if (image == null || image.isEmpty()) {
            return null;
        }

        String contentType = image.getContentType();

        String extension;

        if ("image/jpeg".equals(contentType)) {
            extension = ".jpg";
        } else if ("image/png".equals(contentType)) {
            extension = ".png";
        } else if ("image/webp".equals(contentType)) {
            extension = ".webp";
        } else {
            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed");
        }

        Files.createDirectories(uploadDir);

        String fileName = UUID.randomUUID() + extension;

        Path destination = uploadDir.resolve(fileName);

        Files.copy(image.getInputStream(), destination);

        return "/uploads/images/" + fileName;
    }
}
