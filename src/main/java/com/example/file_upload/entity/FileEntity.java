package com.example.file_upload.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "file_entity")
public class FileEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String uploader;
    private LocalDateTime uploadedAt;
    private String metadata;

    @Lob
    private byte[] fileData;

    public FileEntity(String fileName, String fileType, String uploader, LocalDateTime uploadedAt, String metadata, byte[] fileData) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploader = uploader;
        this.uploadedAt = uploadedAt;
        this.metadata = metadata;
        this.fileData = fileData;
    }

}
