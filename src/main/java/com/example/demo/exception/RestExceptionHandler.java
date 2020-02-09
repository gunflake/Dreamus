package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {NotFoundPlaylist.class})
    public ResponseEntity invalidAccount(NotFoundPlaylist ex) {
        return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
