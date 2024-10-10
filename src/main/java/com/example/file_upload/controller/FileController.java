package com.example.file_upload.controller;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
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
    public ResponseEntity<List<FileDto>> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("uploader") String uploader) {
        try {
            logger.info("File Controller - Uploadin file: {}, by uploader {}", file.getOriginalFilename(), uploader);
            List<FileDto> files = fileService.uploadFile(file, uploader);
            logger.info("File Controller - File uploaded successfully: {}", file.getOriginalFilename());

            return ResponseEntity.ok(files);
        } catch (IOException e) {
            logger.error("File Controller - Error on uploading file: {}", file.getOriginalFilename());
            logger.error(e.getMessage());

            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            logger.error("File Controller - Invalid file type: {}", file.getOriginalFilename());
            logger.error((e.getMessage()));
            return ResponseEntity.status(400).body(null);
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
        logger.info("File Controller - Controller: building list of files uploaded by: {}", uploader);
        List<FileDto> files = fileService.getFilesByUploader(uploader);

        if(files.isEmpty()) {
            logger.warn("File Controller - No files found for uplaoder: {}", uploader);
        }

        logger.info("File Controller - Found {} files for uploader: {}", files.size(), uploader);
        return ResponseEntity.ok(files);
    }

}
