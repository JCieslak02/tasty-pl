package com.jcieslak.tastypl.exception;

public class EntityAlreadyExistsException extends IllegalArgumentException{
    public EntityAlreadyExistsException(String s) {
        super(s);
    }
}
