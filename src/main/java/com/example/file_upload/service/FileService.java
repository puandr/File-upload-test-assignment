package com.example.file_upload.service;

import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String uploadFile(MultipartFile file, String uploader) {
        if (isInvalidFileType(file)) {
            //TODO logging, negative answer
            throw new RuntimeException("File type not allowed");
        }

        FileEntity fileEntity = new FileEntity(
                file.getOriginalFilename(),
                file.getContentType(),
                uploader,
                LocalDateTime.now()
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

}
