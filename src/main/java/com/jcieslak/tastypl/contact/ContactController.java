package com.jcieslak.tastypl.contact;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="api/v1/contact")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<Contact> getContacts(){
        return contactService.getContacts();
    }

    @PostMapping
    public void addContact(@RequestBody Contact contact){
        contactService.addContact(contact);
    }

    @DeleteMapping(path = "{contactId}")
    public void deleteContact(@PathVariable("contactId") Long id){
        contactService.deleteContact(id);
    }

    @PutMapping(path = "{contactId}")
    public void updateContact(@PathVariable("contactId") Long id, @RequestBody Contact contact){
        contactService.updateContact(id, contact);
    }
}
