package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PrincipalIsNotAnOwnerException extends RuntimeException{
    public PrincipalIsNotAnOwnerException() {
        super("User is forbidden from this resource");
    }
}
