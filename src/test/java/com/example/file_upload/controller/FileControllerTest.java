package com.example.file_upload.controller;

import com.example.file_upload.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Base64;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        when(fileService.uploadFile(file, uploader)).thenReturn("\"File uploaded successfully\"");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload")
                        .file(file)
                        .param("uploader", uploader)
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("\"File uploaded successfully\""));
    }

    @Test
    void uploadFile_withInvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                MediaType.IMAGE_JPEG_VALUE, "Invalid file".getBytes());

        String uploader = "Andrei";
        when(fileService.uploadFile(file, uploader)).thenReturn("File type not allowed");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload")
                        .file(file)
                        .param("uploader", uploader)
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("File type not allowed"));
    }
}
