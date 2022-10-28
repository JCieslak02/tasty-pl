package com.jcieslak.tastypl.address;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public Address getAddressById(Long id){
        return addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address with id: " + id + " not found"));
    }

    public List<Address> getAddresses(){
        return addressRepository.findAll();
    }

    public void addAddress(Address address){
        List<Address> addresses = getAddresses();

        for(Address foundAddresses : addresses){
            if(address.equals(foundAddresses)){
                //TODO: create custom exception
                throw new RuntimeException("Address already exists in db");
            }
        }

        addressRepository.save(address);
    }

    public void deleteAddress(Long id){
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

    public void updateAddress(Long id, Address newAddress){
        Address address = getAddressById(id);

        address.setCountry(newAddress.getCountry());
        address.setRegion(newAddress.getRegion());
        address.setZipCode(newAddress.getZipCode());
        address.setCity(newAddress.getCity());
        address.setStreet(newAddress.getStreet());
        address.setBuildingNumber(newAddress.getBuildingNumber());
        address.setSecondaryNumber(newAddress.getSecondaryNumber());

        addressRepository.save(address);
    }
}
