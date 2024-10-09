package com.example.file_upload.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class FileDto implements Serializable {
    private Long id;
    private String fileName;
    private String fileType;
    private String uploader;
    private LocalDateTime uploadedAt;
    private String metadata;

    // Constructor
    public FileDto(Long id, String fileName, String fileType, String uploader, LocalDateTime uploadedAt, String metadata) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
        this.metadata = metadata;
    }
}
