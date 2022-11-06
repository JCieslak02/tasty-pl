package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String type) {
        super(String.format("This %s already exists", type));
    }
}
