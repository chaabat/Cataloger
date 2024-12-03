package com.Cataloger.exception;

import java.time.LocalDateTime;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.Cataloger.dto.response.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalHandlerException {
@ExceptionHandler(value = {MethodArgumentNotValidException.class})
public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setError("Validation failed");
    errorResponse.setMessage(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(value = {EntityNotFoundException.class})
public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
    errorResponse.setError("Entity not found");
    errorResponse.setMessage(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(value = {CategoryNotFoundException.class})
public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
    errorResponse.setError("Category not found");
    errorResponse.setMessage(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(value = {CategoryAlreadyExsistsException.class})
public ResponseEntity<ErrorResponse> handleCategoryAlreadyExsistsException(CategoryAlreadyExsistsException ex) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setError("Category already exists");
    errorResponse.setMessage(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}
}
