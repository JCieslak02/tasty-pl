package com.jcieslak.tastypl.contact;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts(){
        List<Contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact){
        Contact savedContact = contactService.createContact(contact);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable("contactId") Long id){
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{contactId}")
    public ResponseEntity<Contact> updateContact(@PathVariable("contactId") Long id, @RequestBody Contact contact){
        Contact updatedContact = contactService.updateContact(id, contact);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }
}
