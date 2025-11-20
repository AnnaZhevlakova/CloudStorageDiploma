package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.example.CloudStorageDiploma.dto.LoginRequest;
import com.example.CloudStorageDiploma.services.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Scope("request")
@RestController
@RequestMapping("/cloud")
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var result = userService.getAuthorities(loginRequest);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto("Не верный логин или пароль.", HttpStatus.UNAUTHORIZED));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);

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
