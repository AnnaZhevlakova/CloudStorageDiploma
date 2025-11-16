package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.example.CloudStorageDiploma.dto.LoginRequest;
import com.example.CloudStorageDiploma.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cloud")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // TODO: Implement authentication logic
            // Validate credentials, generate token
            String authToken = "generated-auth-token"; // Replace with actual token generation

            return ResponseEntity.ok(new LoginResponse(authToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDto("Bad credentials", 400));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        try {
            // TODO: Implement logout logic (invalidate token)
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
