package com.example.CloudStorageDiploma.exceptionHandler;

import com.example.CloudStorageDiploma.StackTraceUtil;
import com.example.CloudStorageDiploma.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerGlobalException(Exception ex) throws Exception {
        logger.error(String.format("message: %s | stackTrace: %s", ex.getMessage(), StackTraceUtil.getStackTraceAsString(ex)));
        var error = new ErrorDto();
        error.setMessage("Что то пошло не так.");
        error.setId(500);

        ObjectMapper mapper = new ObjectMapper();

        String responseJson = mapper.writeValueAsString(error);
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(responseJson);
    }

}
