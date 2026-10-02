package com.magmutual.userservice.exception;

public class CsvParsingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}