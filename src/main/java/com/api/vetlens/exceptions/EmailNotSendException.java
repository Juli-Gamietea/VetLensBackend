package com.api.vetlens.exceptions;

public class EmailNotSendException extends RuntimeException {
    public EmailNotSendException(String message) {
        super(message);
    }
}
