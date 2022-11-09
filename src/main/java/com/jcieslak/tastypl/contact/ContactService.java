package com.jcieslak.tastypl.contact;

import com.jcieslak.tastypl.exception.HasNonUniqueFieldsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private static final String CONTACT = "contact";

    public Contact getContactById(Long id){
        return contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CONTACT, id));
    }

    public List<Contact> getContacts(){
        return contactRepository.findAll();
    }

    public Contact createContact(Contact contact){
        //checks whether provided contact has null fields, if so, throws a hasNullFields exception
        checkContactForNullFields(contact);

        //both email and tel must be unique, so that's checked in called method
        if(hasNonUniqueFields(contact)) throw new HasNonUniqueFieldsException(CONTACT);

        return contactRepository.save(contact);
    }

    public void deleteContact(Long id){
        Contact contact = getContactById(id);
        contactRepository.delete(contact);
    }

    public Contact updateContact(Long id, Contact newContact){
        Contact contact = getContactById(id);

        //checks whether provided contact has null fields, if so, throws a hasNullFields exception
        checkContactForNullFields(newContact);

        //both email and tel must be unique, so that's checked in called method
        if(hasNonUniqueFields(newContact)) throw new HasNonUniqueFieldsException(CONTACT);

        contact.setEmail(newContact.getEmail());
        contact.setTel(newContact.getTel());

        return contactRepository.save(contact);
    }

    public void checkContactForNullFields(Contact contact){
        if(contact.getEmail() == null || contact.getTel() == null) throw new HasNullFieldsException(CONTACT);
    }
    public boolean hasNonUniqueFields(Contact contact){
        List<Contact> contacts = getContacts();

        for(Contact dbContact : contacts){
            if(dbContact.getTel().equals(contact.getTel()) || dbContact.getEmail().equals(contact.getEmail())){
                return true;
            }
        }

        return false;
    }
}
