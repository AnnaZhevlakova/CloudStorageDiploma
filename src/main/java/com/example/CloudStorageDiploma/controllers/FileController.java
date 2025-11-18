package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import com.example.CloudStorageDiploma.services.FileService;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Scope("request")
@RestController
@RequestMapping("/cloud")
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {

        try {
            // TODO: Validate auth token
            // TODO: Save file to storage
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Error input data", 400));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(new ErrorDto("Unauthorized error", 401));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDto("Error upload file", 500));
        }
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

        try {
            // TODO: Validate auth token
            // TODO: Retrieve file from storage
            Resource fileResource = null; // Replace with actual file resource

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(fileResource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Error input data", 400));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(new ErrorDto("Unauthorized error", 401));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDto("Error upload file", 500));
        }
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