package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HasNonUniqueFieldsException extends RuntimeException{
    public HasNonUniqueFieldsException(String type) {
        super(String.format("This %s has at least 1 non unique field", type));
    }
}
