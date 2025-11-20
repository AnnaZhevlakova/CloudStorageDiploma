package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import com.example.CloudStorageDiploma.services.FileService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Scope("request")
@RestController
@RequestMapping("/cloud")
@Valid
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto("Пустой файл", HttpStatus.BAD_REQUEST));
        }
        var fileData = file.getBytes();
        fileService.uploadFile(filename, fileData, 1, file.getSize());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        fileService.deleteFile(filename, 1);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        var result = fileService.downloadFile(filename, 1);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/file")
    public ResponseEntity<?> renameFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody RenameRequest renameRequest) {
        fileService.renameFile(filename, renameRequest, 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFileList(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "limit", required = false) Integer limit) {
        var result = fileService.getFileList(1, limit);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}