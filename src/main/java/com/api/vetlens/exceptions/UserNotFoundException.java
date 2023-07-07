package com.api.vetlens.exceptions;

import lombok.Getter;
import lombok.Setter;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
