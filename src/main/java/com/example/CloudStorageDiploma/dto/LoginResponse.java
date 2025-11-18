package com.example.CloudStorageDiploma.dto;


public class LoginResponse {
    private String authToken;


    public LoginResponse() {
    }

    public LoginResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
