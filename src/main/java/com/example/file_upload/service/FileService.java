package com.example.file_upload.service;

import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String uploadFile(MultipartFile file, String uploader) {
        if (isInvalidFileType(file)) {
            //TODO logging, correct negative response
//            throw new RuntimeException("File type not allowed");
            return "File type not allowed";
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String metadata = fetchMetadataFromAPI(fileExtension);

        FileEntity fileEntity = new FileEntity(
                file.getOriginalFilename(),
                file.getContentType(),
                uploader,
                LocalDateTime.now(),
                metadata
        );

        fileRepository.save(fileEntity);

        //TODO correct response
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
        return filename.substring(filename.lastIndexOf(".") + 1);
    }


    private String fetchMetadataFromAPI(String extension) {
        String apiUrl = "https://httpbin.org/post";

        Map<String, String> requestData = new HashMap<>();
        requestData.put("extension", extension);

        Map<String, Object> response = restTemplate.postForObject(apiUrl, requestData, Map.class);

        if (response != null) {
            return response.get("data").toString() + " - should be useful metadata";
        }

        return "No metadata";
    }

}
