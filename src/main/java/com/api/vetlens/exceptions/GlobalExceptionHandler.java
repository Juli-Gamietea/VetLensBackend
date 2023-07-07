package com.api.vetlens.exceptions;

import com.api.vetlens.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> userNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(404).body(new ExceptionDTO(exception.getMessage(), exception.getClass().getName(), 404));
    }

}
