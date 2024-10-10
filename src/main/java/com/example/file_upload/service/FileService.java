package com.example.file_upload.service;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final FileRepository fileRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @CacheEvict(value = "filesByUploader", key = "#uploader")
    public String uploadFile(MultipartFile file, String uploader) throws IOException {
        if (isInvalidFileType(file)) {
            logger.warn("FileService - File type is invalid: {}", file.getContentType());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type not allowed");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String metadata = fetchMetadataFromAPI(fileExtension);

        FileEntity fileEntity = new FileEntity(
                file.getOriginalFilename(),
                file.getContentType(),
                uploader,
                LocalDateTime.now(),
                metadata,
                file.getBytes()
        );

        fileRepository.save(fileEntity);

        logger.info("FileService - File upload successfully: {}, by uploader: ", file.getOriginalFilename(), uploader);
        return "File uploaded succesfully";
    }

    private boolean isInvalidFileType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType.equals("image/jpeg") || contentType.equals("image/gif") || contentType.equals("image/png")) {
            return true;
        }
        return false;
    }

    private String getFileExtension(String filename){
        if (filename == null || !filename.contains(".")) {
            logger.warn("FileService - Filename has no extension: {}", filename);
            return "unknown";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }


    private String fetchMetadataFromAPI(String extension) {
        String apiUrl = "https://httpbin.org/post";
        Map<String, String> requestData = new HashMap<>();
        requestData.put("extension", extension);

        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, requestData, Map.class);

            if (response != null && response.containsKey("data")) {
                logger.info("FileService - Metadata fetched succesfully for: {}", extension);
                return response.get("data").toString() + " - should be useful metadata";
            } else {
                logger.warn("FileService - No metadata fetched for extension: {}", extension);
                return "No metadata";
            }
        } catch (Exception e) {
            logger.error("FileService - Error fetching metada: {}", e.getMessage());
            return "Error fetching metadata";
        }
    }

    @Cacheable(value = "filesByUploader", key = "#uploader")
    public List<FileDto> getFilesByUploader(String uploader) {
        logger.info("FileService - Fetching files for uploader: {}", uploader);
        List<FileEntity> fileEntities = fileRepository.findByUploader(uploader);

        if (fileEntities.isEmpty()) {
            logger.warn("FileService - No files found for uploader: {}", uploader);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uploader not found");
        }

        List<FileDto> fileDtos = new ArrayList<>();
        for (FileEntity fileEntity : fileEntities) {
            FileDto fileDto = convertToFileDto(fileEntity);
            fileDtos.add(fileDto);
        }

        logger.info("FileService - Found {} files by uploader: {}", fileDtos.size(), uploader);
        return fileDtos;
    }

    private FileDto convertToFileDto(FileEntity fileEntity) {
        return new FileDto(
                fileEntity.getId(),
                fileEntity.getFileName(),
                fileEntity.getFileType(),
                fileEntity.getUploader(),
                fileEntity.getUploadedAt(),
                fileEntity.getMetadata()
        );
    }

}
