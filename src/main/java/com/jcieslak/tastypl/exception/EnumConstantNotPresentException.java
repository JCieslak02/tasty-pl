package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EnumConstantNotPresentException extends RuntimeException{
    public EnumConstantNotPresentException(String constant) {
        super(String.format("%s is not a constant", constant));
    }
}
