package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public NotFoundException(String type, Long id) {
        super(String.format("%s with id: %d not found", type, id));
    }
}
