package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.example.CloudStorageDiploma.dto.FileInfo;
import com.example.CloudStorageDiploma.dto.RenameRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public class FileController {

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

        try {
            // TODO: Validate auth token
            // TODO: Delete file from storage
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Error input data", 400));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(new ErrorDto("Unauthorized error", 401));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDto("Error delete file", 500));
        }
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

        try {
            // TODO: Validate auth token
            // TODO: Rename file in storage
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Error input data", 400));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(new ErrorDto("Unauthorized error", 401));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDto("Error upload file", 500));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFileList(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "limit", required = false) Integer limit) {

        try {
            // TODO: Validate auth token
            // TODO: Retrieve file list from storage
            List<FileInfo> files = List.of(); // Replace with actual file list

            if (limit != null && limit > 0) {
                // Apply limit if provided
                files = files.stream().limit(limit).toList();
            }

            return ResponseEntity.ok(files);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Error input data", 400));
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(new ErrorDto("Unauthorized error", 401));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDto("Error getting file list", 500));
        }
    }
}