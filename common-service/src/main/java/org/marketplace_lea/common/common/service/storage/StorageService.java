package org.marketplace_lea.common.common.service.storage;

import org.marketplace_lea.common.common.exceptions.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    void store(MultipartFile file);

    void store(MultipartFile file, String filename);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename) throws StorageFileNotFoundException;

    void delete(String filename) throws IOException;

    void deleteV2(String filename);

    void deleteAll();
}
