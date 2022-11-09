package com.jcieslak.tastypl.client;

import com.jcieslak.tastypl.contact.Contact;
import com.jcieslak.tastypl.contact.ContactService;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ContactService contactService;
    private static final String CLIENT = "client";

    public Client getClientById(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CLIENT, id));
    }

    public List<Client> getClients(){
        return clientRepository.findAll();
    }

    //there's no need to check for duplicates in this class, the only unique thing for clients is contact, and it's validated in contactService class
    public Client createClient(Client client){
        //checks if there are null fields, if so, throws a hasNullFields exception
        checkForNullFields(client);
        //this method adds a contact to db after validating it for nonUnique fields and duplicates
        contactService.createContact(client.getContact());

        return clientRepository.save(client);
    }

    public void deleteClient(long id){
        //called method takes care of not found exception
        Client client = getClientById(id);
        clientRepository.delete(client);
    }

    public Client updateClient(long id, Client newClient){
        //this method gets the client entity from db, and takes care of not found exception
        Client client = getClientById(id);

        //checks if there are null fields, if so, throws a hasNullFields exception
        checkForNullFields(newClient);

        //this method updates checks for contact uniqueness and updates it in db || throws exception if non unique
        contactService.updateContact(client.getContact().getId(), newClient.getContact());

        client.setContact(newClient.getContact());
        client.setFirstName(newClient.getFirstName());
        client.setLastName(newClient.getLastName());

        return clientRepository.save(client);
    }

    public Contact updateClientContact(long id, Contact newContact){
        //this method gets the client entity from db, and takes care of not found exception
        Client client = getClientById(id);

        //this method updates checks for contact uniqueness and updates it in db || throws exception if non unique
        return contactService.updateContact(client.getContact().getId(), newContact);
    }

    public void checkForNullFields(Client client){
        if(client.getFirstName() == null || client.getLastName() == null || client.getContact() == null){
            throw new HasNullFieldsException(CLIENT);
        }
    }

}
