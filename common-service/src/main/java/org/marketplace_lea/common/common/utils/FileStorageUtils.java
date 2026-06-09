package org.marketplace_lea.common.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class FileStorageUtils {

    public static String createNewFileName() {
        return UUID.randomUUID().toString();
    }

    public static String createNewFileName(String originalFileName) {
        String extension = Optional.ofNullable(FilenameUtils.getExtension(originalFileName))
                .orElse("");
        return String.format("%s.%s", originalFileName, extension);

    }

    public static boolean isImageFile(MultipartFile file) {
        return file != null && Objects.requireNonNull(file.getContentType())
                .startsWith("image/");
    }

    public static boolean areImageFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .anyMatch(file -> !isImageFile(file));
    }
}
