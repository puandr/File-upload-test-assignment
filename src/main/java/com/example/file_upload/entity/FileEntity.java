package com.example.file_upload.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class FileEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String uploader;
    private LocalDateTime uploadedAt;

    public FileEntity(String fileName, String fileType, String uploader, LocalDateTime uploadedAt) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
    }

}
