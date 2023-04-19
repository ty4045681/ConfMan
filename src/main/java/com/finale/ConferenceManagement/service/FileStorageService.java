package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.configuration.FileStorageProperties;
import com.finale.ConferenceManagement.interfaces.SaveFileMetadata;
import com.finale.ConferenceManagement.interfaces.SetStoredFileName;
import com.finale.ConferenceManagement.interfaces.SetTargetLocation;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.Presentation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory for file storage", e);
        }
    }

    public <T> T storeFile(T entity, MultipartFile file, SaveFileMetadata<T> saveFileMetadata, SetStoredFileName<T> setStoredFileName, SetTargetLocation setTargetLocation) throws RuntimeException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file path: " + fileName);
            }

            Path targetLocation = setTargetLocation.setTargetLocation(this.fileStorageLocation, fileName);
            // Create directories if they don't exist
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            entity = setStoredFileName.setStoredFileName(entity, fileName);

            return saveFileMetadata.save(entity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + fileName, e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }
}
