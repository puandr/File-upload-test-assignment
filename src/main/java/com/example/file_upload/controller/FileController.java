package com.example.file_upload.controller;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    //TODO uploader - is it a good naming?
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("uploader") String uploader) {
        try {
            String responseMessage = fileService.uploadFile(file, uploader);
            return ResponseEntity.ok(responseMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage() );
        }
    }

    @GetMapping("/uploaded-by")
    public ResponseEntity<List<FileDto>> getFileByUploader(@RequestParam("uploader") String uploader) {
        List<FileDto> files = fileService.getFilesByUploader(uploader);
        return ResponseEntity.ok(files);
        //TODO if no user?
    }

}
