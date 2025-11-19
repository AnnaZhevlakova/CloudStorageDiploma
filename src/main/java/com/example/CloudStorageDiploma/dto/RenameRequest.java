package com.example.CloudStorageDiploma.dto;


import jakarta.validation.constraints.NotBlank;

public class RenameRequest {

    @NotBlank
    private String name;

    public RenameRequest() {
    }

    public RenameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
