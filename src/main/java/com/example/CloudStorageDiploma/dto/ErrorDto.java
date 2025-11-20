package com.example.CloudStorageDiploma.dto;


import org.springframework.http.HttpStatus;

public class ErrorDto {
    private String message;
    private Integer id;

    public ErrorDto() {
    }

    public ErrorDto(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    public ErrorDto(String message, HttpStatus httpStatus) {
        this.message = message;
        this.id = httpStatus.ordinal();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}