package com.example.file_upload.service;

import com.example.file_upload.dto.FileDto;
import com.example.file_upload.entity.FileEntity;
import com.example.file_upload.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    void getFilesByUploaderReturnsFiles() {
        String uploader = "Andrei";
        List<FileEntity> files = Arrays.asList(
                new FileEntity("file1.txt", "text/plain", uploader, LocalDateTime.now(), "MetaData1", new byte[0]),
                new FileEntity("file2.txt", "text/plain", uploader, LocalDateTime.now(), "MetaData2", new byte[0])
        );

        when(fileRepository.findByUploader(uploader)).thenReturn(files);

        List<FileDto> result = fileService.getFilesByUploader(uploader);

        assertThat(result).hasSize(2);
    }
}
