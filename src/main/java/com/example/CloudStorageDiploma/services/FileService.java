package com.example.CloudStorageDiploma.services;

import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.dto.GetFileResponse;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import com.example.CloudStorageDiploma.repositories.FileInfoRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Scope("request")
@Service
public class FileService {
    private FileInfoRepository fileInfoRepository;

    public FileService(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }


    public boolean uploadFile(String filename, byte[] fileData, long userId) {
        throw new UnsupportedOperationException("Method not implemented yet");

    }


    public boolean deleteFile(String filename, long userId) {
        filename = filename.trim().toLowerCase();
        fileInfoRepository.deleteByFileNameAndUserId(filename, userId);
        return true;
    }


    public GetFileResponse downloadFile(String filename, long userId) {
        filename = filename.trim().toLowerCase();
        var fileInfo = fileInfoRepository.findByFileNameAndUserId(filename, userId);
        if (fileInfo == null) {
            return null;
        }
        var getResponse = new GetFileResponse();
        getResponse.setHash(fileInfo.getHash());
        getResponse.setFile(Base64Example.bytesToBase64(fileInfo.getFileData()));
        return getResponse;

    }


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
