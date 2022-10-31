package com.jcieslak.tastypl.address;

import com.jcieslak.tastypl.address.exception.AddressAlreadyExistsException;
import com.jcieslak.tastypl.address.exception.AddressNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public Address getAddressById(Long id){
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
    }

    public List<Address> getAddresses(){
        return addressRepository.findAll();
    }

    //this method is also used in updateAddress to reduce duplicate code, helps to validate new address to protect from duplicates
    public void addAddress(Address address){
        List<Address> addresses = getAddresses();

        for(Address foundAddresses : addresses){
            if(address.equals(foundAddresses)){
                throw new AddressAlreadyExistsException();
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

        addAddress(address);
    }
}
