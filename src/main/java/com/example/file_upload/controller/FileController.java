package com.example.file_upload.controller;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Upload a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("uploader") String uploader) {
        try {
            String responseMessage = fileService.uploadFile(file, uploader);
            return ResponseEntity.ok(responseMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage() );
        }
    }

    @Operation(summary = "Get files uploaded by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of files",
                    content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "Uploader not found")
    })
    @GetMapping("/uploaded-by")
    public ResponseEntity<List<FileDto>> getFileByUploader(@RequestParam("uploader") String uploader) {
        List<FileDto> files = fileService.getFilesByUploader(uploader);
        return ResponseEntity.ok(files);
        //TODO if no user?
    }

}
