package com.jcieslak.tastypl.contact.exception;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(Long id) {
        super("Contact with id: " + id + " doesn't exist in db");
    }
}
