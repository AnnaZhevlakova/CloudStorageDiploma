package com.example.CloudStorageDiploma.dto;


public class GetFileResponse {
    private String hash;
    private String file;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
