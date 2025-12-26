package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.components.JwtUtil;
import com.example.CloudStorageDiploma.dto.*;
import com.example.CloudStorageDiploma.services.FileService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Scope("request")
@RestController
@RequestMapping("/cloud")
@Valid
public class FileController {
    private FileService fileService;
    private JwtUtil jwtUtil;

    public FileController(FileService fileService, JwtUtil jwtUtil) {
        this.fileService = fileService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto("Пустой файл", HttpStatus.BAD_REQUEST));
        }
        var fileData = file.getBytes();
        var userId = jwtUtil.extractUserId(authToken);
        fileService.uploadFile(filename, fileData, userId, file.getSize());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwtUtil.extractUserId(authToken);
        fileService.deleteFile(filename, userId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetFileResponse.class)
                    )
            )
    })
    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwtUtil.extractUserId(authToken);
        var result = fileService.downloadFile(filename, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/file")
    public ResponseEntity<?> renameFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody RenameRequest renameRequest) {
        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwtUtil.extractUserId(authToken);
        fileService.renameFile(filename, renameRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FileInfoDto.class))
                    )
            )
    })
    @GetMapping("/list")
    public ResponseEntity<?> getFileList(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "limit", required = false) Integer limit) {
        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwtUtil.extractUserId(authToken);
        var result = fileService.getFileList(userId, limit);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}