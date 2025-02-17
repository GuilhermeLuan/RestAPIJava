package com.example.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Executable;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultErrorMessage> handleBadRequestException(BadRequestException e) {
        var error = new DefaultErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
