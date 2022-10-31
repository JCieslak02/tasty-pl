package com.jcieslak.tastypl.address.exception;

public class AddressAlreadyExistsException extends RuntimeException{
    public AddressAlreadyExistsException() {
        super("This address already exists");
    }
}
