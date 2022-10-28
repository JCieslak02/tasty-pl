package com.jcieslak.tastypl.contact;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact getContactById(Long id){
        //TODO: custom exception
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact with id : " + id + " doesn't exist"));
    }

    public List<Contact> getContacts(){
        return contactRepository.findAll();
    }

    public void addContact(Contact contact){
        //TODO: custom exception
        Optional<Contact> queriedContact = contactRepository.findContactByEmail(contact.getEmail());

        if(queriedContact.isPresent()){
            throw new IllegalArgumentException("This contact already exists");
        }

        contactRepository.save(contact);
    }

    public void deleteContact(Long id){
        Contact contact = getContactById(id);
        contactRepository.delete(contact);
    }

    public void updateContact(Long id, Contact newContact){
        Contact contact = getContactById(id);

        contact.setEmail(newContact.getEmail());
        contact.setTel(newContact.getTel());
    }
}
