package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberOrEmailException extends RuntimeException{
    public InvalidPhoneNumberOrEmailException() {
        super("Provided phone number or email is invalid");
    }
}
