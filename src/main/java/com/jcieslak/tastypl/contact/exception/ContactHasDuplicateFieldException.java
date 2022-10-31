package com.jcieslak.tastypl.contact.exception;

public class ContactHasDuplicateFieldException extends RuntimeException{
    public ContactHasDuplicateFieldException() {
        super("Contact has either a duplicate email or telephone number");
    }
}
