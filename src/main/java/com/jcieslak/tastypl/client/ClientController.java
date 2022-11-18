package com.jcieslak.tastypl.client;

import com.jcieslak.tastypl.contact.Contact;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(){
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping( "/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable(name = "clientId") Long id){
        Client client = clientService.getClientById(id);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        Client createdClient = clientService.createClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") Long id){
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable("clientId") Long id, @RequestBody Client client){
        Client updatedClient = clientService.updateClient(id, client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PutMapping(path = "/{clientId}/contact")
    public ResponseEntity<Contact> updateClientContact(@PathVariable("clientId") Long id, @RequestBody Contact newContact){
        Contact updatedContact = clientService.updateClientContact(id, newContact);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

}
