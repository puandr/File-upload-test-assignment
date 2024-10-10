package com.example.file_upload.controller;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void uploadDataWitValidData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "some important text".getBytes());

        String uploader = "Andrei";
        List<FileDto> files = Arrays.asList(
                new FileDto(1L, "test.txt", "text/plain", uploader, LocalDateTime.now(), "Sample Metadata")
        );

        when(fileService.uploadFile(file, uploader)).thenReturn(files);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload")
                        .file(file)
                        .param("uploader", uploader)
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("test.txt"))
                .andExpect(jsonPath("$[0].uploader").value("Andrei"));
    }

    @Test
    void uploadFileWithInvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                MediaType.IMAGE_JPEG_VALUE, "Invalid file".getBytes());

        String uploader = "Andrei";

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type not allowed"))
                .when(fileService).uploadFile(file, uploader);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload")
                        .file(file)
                        .param("uploader", uploader)
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
