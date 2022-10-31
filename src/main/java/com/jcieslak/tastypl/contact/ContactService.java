package com.jcieslak.tastypl.contact;

import com.jcieslak.tastypl.contact.exception.ContactHasDuplicateFieldException;
import com.jcieslak.tastypl.contact.exception.ContactNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact getContactById(Long id){
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public List<Contact> getContacts(){
        return contactRepository.findAll();
    }

    public void addContact(Contact contact){
        Optional<Contact> queriedByEmailContact = contactRepository.findContactByEmail(contact.getEmail());
        Optional<Contact> queriedByTelContact = contactRepository.findContactByTel(contact.getTel());

        if(queriedByEmailContact.isPresent() || queriedByTelContact.isPresent()){
            throw new ContactHasDuplicateFieldException();
        }

        contactRepository.save(contact);
    }

    public void deleteContact(Long id){
        Contact contact = getContactById(id);
        contactRepository.delete(contact);
    }

    public void updateContact(Long id, Contact newContact){
        Contact contact = getContactById(id);

        List<Contact> allContacts = getContacts();


        for(Contact dbContact : allContacts){
            if(dbContact.equals(contact)) continue;

            if(dbContact.getEmail().equals(newContact.getEmail()) || dbContact.getTel().equals(newContact.getTel())){
                throw new ContactHasDuplicateFieldException();
            }
        }

        contact.setEmail(newContact.getEmail());
        contact.setTel(newContact.getTel());

        contactRepository.save(contact);
    }
}
