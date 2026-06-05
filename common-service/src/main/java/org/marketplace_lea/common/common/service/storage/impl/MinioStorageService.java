package org.marketplace_lea.common.common.service.storage.impl;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import org.marketplace_lea.common.common.exceptions.StorageException;
import org.marketplace_lea.common.common.exceptions.StorageFileNotFoundException;
import org.marketplace_lea.common.common.service.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
public class MinioStorageService implements StorageService {
    private final MinioClient minioClient;
    private final String bucketName;

    @Autowired
    public MinioStorageService(MinioClient minioClient, String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    @Override
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Created bucket: {}", bucketName);
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException | InvalidResponseException e) {
            log.error("Could not initialize Minio storage: {}", e.getMessage(), e);
            throw new StorageException("Could not initialize Minio storage", e);
        }
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
            
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(filename)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                log.info("Successfully saved file to Minio: {}/{}", bucketName, filename);
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException | InvalidResponseException e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build());
            List<Path> paths = new ArrayList<>();
            for (Result<Item> result : results) {
                paths.add(Path.of(result.get().objectName()));
            }
            return paths.stream();
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException  | InvalidResponseException e) {
            log.error("Failed to read stored files: {}", e.getMessage(), e);
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return Path.of(bucketName).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException, StorageFileNotFoundException {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.GET)
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            Resource resource = new UrlResource(url);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.error("Could not read file: {}", filename);
                throw new StorageException("Could not read file: " + filename);
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException  | InvalidResponseException e) {
            log.error("File not found: {}", e.getMessage(), e);
            throw new StorageFileNotFoundException("File not found: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            log.info("Deleted file: {}/{}", bucketName, filename);
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | NoSuchAlgorithmException | ServerException |
                 XmlParserException  | InvalidResponseException e) {
            log.error("Failed to delete file: {}", e.getMessage(), e);
            throw new IOException("Failed to delete file", e);
        }
    }

    @Override
    public void deleteV2(String filename) {
        try {
            delete(filename);
        } catch (IOException e) {
            log.error("Error while deleting file: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            minioClient.removeBucket(
                    RemoveBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            log.info("Deleted bucket: {}", bucketName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 NoSuchAlgorithmException | ServerException | XmlParserException | InvalidResponseException |
                 IOException e) {
            log.error("Failed to delete bucket: {}", e.getMessage(), e);
            throw new StorageException("Failed to delete bucket", e);
        }
    }
}
