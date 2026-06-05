package org.marketplace_lea.common.common.service.storage.impl;

import org.marketplace_lea.common.common.exceptions.StorageException;
import org.marketplace_lea.common.common.exceptions.StorageFileNotFoundException;
import org.marketplace_lea.common.common.service.storage.StorageProperties;
import org.marketplace_lea.common.common.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "filesystem", matchIfMissing = true)
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        store(file, file.getOriginalFilename());
    }

    @Override
    public void store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(filename)))
                    .normalize()
                    .toAbsolutePath();
            if (!destinationFile.getParent().toString().contains(this.rootLocation.toAbsolutePath().toString())) {
                // This is a security check
                log.error("Cannot store file outside root image directory.");
                throw new StorageException("Cannot store file outside root image directory.");
            }
            if (!Files.exists(destinationFile)) {
                Files.createDirectories(destinationFile);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("Successfully saved file to: {}", destinationFile);
            }
        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try (Stream<Path> stream = Files.walk(this.rootLocation, 1)) {
            return stream.filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException, StorageFileNotFoundException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()) {
                if (resource.isReadable()) {
                    return resource;
                } else {
                    log.error("Could not read file: {}", filename);
                    throw new StorageException("Could not read file: " + filename);
                }
            } else {
                log.error("File Not found: {}", filename);
                throw new StorageFileNotFoundException("File Not found: " + filename);
            }
        } catch (MalformedURLException | StorageFileNotFoundException e) {
            log.error("File Not found: {}", e.getMessage(), e);
            throw new StorageFileNotFoundException("File not found: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        FileSystemUtils.deleteRecursively(load(filename));
    }

    @Override
    public void deleteV2(String filename) {
        try {
            FileSystemUtils.deleteRecursively(load(filename));
        } catch (IOException e) {
            log.error("Error while deleting Image: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        if (!Files.exists(rootLocation)) {
            try {
                Files.createDirectories(rootLocation);
            } catch (IOException e) {
                log.error("Could not initialize storage: {}", e.getMessage(), e);
                throw new StorageException("Could not initialize storage", e);
            }
        }
    }
}
