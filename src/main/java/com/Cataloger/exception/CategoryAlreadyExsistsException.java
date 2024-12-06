package com.Cataloger.exception;

public class CategoryAlreadyExsistsException extends RuntimeException {
    public CategoryAlreadyExsistsException(String message) {
        super(message);
    }
}
