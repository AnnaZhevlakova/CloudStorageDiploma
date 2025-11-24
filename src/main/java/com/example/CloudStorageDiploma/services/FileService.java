package com.example.CloudStorageDiploma.services;

import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.dto.GetFileResponse;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import com.example.CloudStorageDiploma.entities.FileInfo;
import com.example.CloudStorageDiploma.repositories.FileInfoRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;


@Scope("request")
@Service
public class FileService {
    private FileInfoRepository fileInfoRepository;
    private static final Logger logger = LogManager.getLogger(FileService.class);

    public FileService(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }

    @Transactional
    public boolean uploadFile(String filename, byte[] fileData, long userId, long fileSize) throws Exception {
        var newFile = new FileInfo();
        newFile.setFileName(filename.trim().toLowerCase());
        newFile.setUserId(userId);
        newFile.setUploadDate(ZonedDateTime.now(ZoneOffset.UTC));
        newFile.setFileSize(fileSize);
        newFile.setFileData(fileData);
        newFile.setHash(HelperService.sha256(fileData));
        fileInfoRepository.save(newFile);

        return true;
    }

    @Transactional
    public boolean deleteFile(String filename, long userId) {
        filename = filename.trim().toLowerCase();
        fileInfoRepository.deleteByFileNameAndUserId(filename, userId);
        return true;
    }


    public GetFileResponse downloadFile(String filename, long userId) {
        filename = filename.trim().toLowerCase();
        var fileInfo = fileInfoRepository.findByFileNameAndUserId(filename, userId);
        if (fileInfo == null) {
            logger.info(String.format("Файл %s не найден. userId %d", filename, userId));
            return null;
        }
        var getResponse = new GetFileResponse();
        getResponse.setHash(fileInfo.getHash());
        getResponse.setFile(HelperService.bytesToBase64(fileInfo.getFileData()));
        return getResponse;

    }

    @Transactional
    public boolean renameFile(String filename, RenameRequest renameRequest, long userId) {
        filename = filename.trim().toLowerCase();
        var newName = renameRequest.getName().trim().toLowerCase();
        fileInfoRepository.updateFileName(filename, newName, userId);
        return true;

    }

    public List<FileInfoDto> getFileList(long userId, Integer limit) {
        var result = limit == null ? fileInfoRepository.getFilesList(userId) : fileInfoRepository.getFilesList(userId, limit, 0);
        return result;

    }

}
