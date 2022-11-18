package com.jcieslak.tastypl.address;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/addresses")
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses(){
        List<Address> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable(name="id") Long id){
        Address address = addressService.getAddressById(id);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address){
        Address createdAddress = addressService.createAddress(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable("addressId") Long id, @RequestBody Address newAddress){
        Address address = addressService.updateAddress(id, newAddress);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
    @DeleteMapping(path="/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Long id){
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
