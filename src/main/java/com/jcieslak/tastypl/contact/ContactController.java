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
    public List<Contact> getContacts(){
        return contactService.getContacts();
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact){
        contactService.createContact(contact);
        return new ResponseEntity<>(contact, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable("contactId") Long id){
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "{contactId}")
    public ResponseEntity<Contact> updateContact(@PathVariable("contactId") Long id, @RequestBody Contact contact){
        contactService.updateContact(id, contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }
}
