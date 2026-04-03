package com.example.clothing4413.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private final Path uploadDir = Paths.get("uploads", "products");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload folder.");
        }
    }

    public String storeProductImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Product image is required.");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        String originalFilename = StringUtils.cleanPath(image.getOriginalFilename() == null ? "image" : image.getOriginalFilename());

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + extension;
        Path destination = uploadDir.resolve(fileName).normalize();

        if (!destination.startsWith(uploadDir)) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store image.");
        }

        return "/uploads/products/" + fileName;
    }
}