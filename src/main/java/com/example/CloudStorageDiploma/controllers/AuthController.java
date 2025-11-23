package com.example.CloudStorageDiploma.controllers;

import com.example.CloudStorageDiploma.components.JwtUtil;
import com.example.CloudStorageDiploma.components.TokenBlacklist;
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
    private JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
        if (!jwtUtil.validateToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        TokenBlacklist.blacklistToken(authToken);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
