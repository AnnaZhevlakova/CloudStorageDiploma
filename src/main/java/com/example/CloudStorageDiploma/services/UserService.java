package com.example.CloudStorageDiploma.services;

import com.example.CloudStorageDiploma.components.JwtUtil;
import com.example.CloudStorageDiploma.dto.LoginRequest;
import com.example.CloudStorageDiploma.dto.LoginResponse;
import com.example.CloudStorageDiploma.repositories.UserRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("request")
@Service
public class UserService {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse getAuthorities(LoginRequest loginRequest) {
        var user = userRepository.findByLoginAndPassword(loginRequest.getLogin());
        if (user == null) {
            return null;
        }
        if (!HelperService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }
        var token = jwtUtil.generateToken(user.getId(), user.getLogin());
        var result = new LoginResponse();
        result.setAuthToken(token);
        return result;
    }


}
