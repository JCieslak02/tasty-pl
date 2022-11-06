package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HasDuplicateFieldsException extends RuntimeException{
    public HasDuplicateFieldsException(String type) {
        super(String.format("This %s has at least 1 duplicate field", type));
    }
}
