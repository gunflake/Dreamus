package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

public class ResponseError {
    @JsonFormat
    private HttpStatus status;

    @JsonFormat
    private String message;

    public ResponseError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
