package com.example.CloudStorageDiploma;

import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.dto.GetFileResponse;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import com.example.CloudStorageDiploma.entities.FileInfo;
import com.example.CloudStorageDiploma.repositories.FileInfoRepository;
import com.example.CloudStorageDiploma.services.FileService;
import com.example.CloudStorageDiploma.services.HelperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @Mock
    private FileInfoRepository fileInfoRepository;

    @InjectMocks
    private FileService fileService;

    private static final long USER_ID = 123L;
    private static final String FILENAME = "testfile.txt";
    private static final String NEW_FILENAME = "newname.jpg";
    private static final byte[] FILE_DATA = "Hello World".getBytes();
    private static final String FILE_HASH = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
    private static final long FILE_SIZE = 12345L;

    @Test
    void uploadFile_Success() throws Exception {

        try (MockedStatic<ZonedDateTime> zonedDateTimeMock = mockStatic(ZonedDateTime.class, withSettings().defaultAnswer(CALLS_REAL_METHODS))) {
            try (MockedStatic<HelperService> helperMock = mockStatic(HelperService.class)) {
                helperMock.when(() -> HelperService.sha256(FILE_DATA)).thenReturn(FILE_HASH);
                FileInfo savedFile = new FileInfo();
                when(fileInfoRepository.save(any(FileInfo.class))).thenAnswer(invocation -> {
                    FileInfo file = invocation.getArgument(0);
                    savedFile.setId(1L);
                    savedFile.setFileName(file.getFileName());
                    savedFile.setUserId(file.getUserId());
                    savedFile.setUploadDate(file.getUploadDate());
                    savedFile.setFileSize(file.getFileSize());
                    savedFile.setFileData(file.getFileData());
                    savedFile.setHash(file.getHash());
                    return savedFile;
                });

                boolean result = fileService.uploadFile(FILENAME, FILE_DATA, USER_ID, FILE_SIZE);

                assertTrue(result);
                verify(fileInfoRepository).save(argThat(file ->
                        FILENAME.equals(file.getFileName()) &&
                                USER_ID == file.getUserId() &&
                                FILE_SIZE == file.getFileSize() &&
                                Arrays.equals(FILE_DATA, file.getFileData()) &&
                                FILE_HASH.equals(file.getHash()) &&
                                file.getUploadDate() != null
                ));
            }
        }
    }

    @Test
    void deleteFile_Success_WhenMethodReturnsVoid() {

        doNothing().when(fileInfoRepository).deleteByFileNameAndUserId(anyString(), anyLong());

        boolean result = fileService.deleteFile("  TestFile.TXT  ", USER_ID);

        assertTrue(result);

        verify(fileInfoRepository).deleteByFileNameAndUserId("testfile.txt", USER_ID);
    }

    @Test
    void downloadFile_FileFound_ReturnsResponse() {

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileData(FILE_DATA);
        fileInfo.setHash(FILE_HASH);

        when(fileInfoRepository.findByFileNameAndUserId(FILENAME, USER_ID)).thenReturn(fileInfo);

        try (MockedStatic<HelperService> helperMock = mockStatic(HelperService.class)) {
            helperMock.when(() -> HelperService.bytesToBase64(FILE_DATA))
                    .thenReturn("SGVsbG8gV29ybGQ=");
            GetFileResponse response = fileService.downloadFile(FILENAME, USER_ID);
            assertNotNull(response);
            assertEquals(FILE_HASH, response.getHash());
            assertEquals("SGVsbG8gV29ybGQ=", response.getFile());
        }
    }

    @Test
    void downloadFile_FileNotFound_ReturnsNull() {
        when(fileInfoRepository.findByFileNameAndUserId(FILENAME, USER_ID)).thenReturn(null);
        GetFileResponse response = fileService.downloadFile(FILENAME, USER_ID);
        assertNull(response);
        verify(fileInfoRepository).findByFileNameAndUserId(FILENAME, USER_ID);
    }

    @Test
    void renameFile_Success() {
        RenameRequest renameRequest = new RenameRequest();
        renameRequest.setName(NEW_FILENAME);
        when(fileInfoRepository.updateFileName(eq(FILENAME), eq(NEW_FILENAME), eq(USER_ID))).thenReturn(1);
        boolean result = fileService.renameFile(FILENAME, renameRequest, USER_ID);
        assertTrue(result);
        verify(fileInfoRepository).updateFileName(FILENAME, NEW_FILENAME, USER_ID);
    }

    @Test
    void getFileList_WithoutLimit_ReturnsFullList() {
        List<FileInfoDto> expectedList = Arrays.asList(
                createFileInfoDto("file1.txt", 100L),
                createFileInfoDto("file2.jpg", 200L)
        );
        when(fileInfoRepository.getFilesList(USER_ID)).thenReturn(expectedList);
        List<FileInfoDto> result = fileService.getFileList(USER_ID, null);
        assertEquals(expectedList, result);
        verify(fileInfoRepository).getFilesList(USER_ID);
    }

    @Test
    void getFileList_WithLimit_ReturnsLimitedList() {
        Integer limit = 5;
        List<FileInfoDto> expectedList = Arrays.asList(
                createFileInfoDto("recent.txt", 50L)
        );
        when(fileInfoRepository.getFilesList(USER_ID, limit, 0)).thenReturn(expectedList);
        List<FileInfoDto> result = fileService.getFileList(USER_ID, limit);
        assertEquals(expectedList, result);
        verify(fileInfoRepository).getFilesList(USER_ID, limit, 0);
    }

    private FileInfoDto createFileInfoDto(String filename, long size) {
        FileInfoDto dto = new FileInfoDto();
        dto.setFilename(filename);
        dto.setSize(size);
        return dto;
    }
}

