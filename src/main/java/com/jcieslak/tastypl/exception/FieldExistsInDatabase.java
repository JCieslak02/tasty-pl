package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FieldExistsInDatabase extends RuntimeException{
    public FieldExistsInDatabase(String entityType, String fieldType) {
        super(String.format("Provided %s has a %s that already exists in database", entityType, fieldType));
    }
}
