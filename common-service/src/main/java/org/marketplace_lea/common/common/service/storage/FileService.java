package org.marketplace_lea.common.common.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	void uploadFile(MultipartFile file);
	void uploadFile(MultipartFile file, String uploadPath);
}