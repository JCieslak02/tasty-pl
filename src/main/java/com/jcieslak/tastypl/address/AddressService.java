package com.jcieslak.tastypl.address;

import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AddressService {
    // this constant is a dum dum solution, I'll change it soon
    public static final String ADDRESS = "address";
    private final AddressRepository addressRepository;

    public Address getAddressById(Long id){
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ADDRESS, id));
    }

    public List<Address> getAddresses(){
        return addressRepository.findAll();
    }

    public Address createAddress(Address address){
        checkAddressForNullFields(address);

        boolean isDuplicate = isAddressADuplicate(address);

        if(isDuplicate) throw new AlreadyExistsException(ADDRESS);

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id){
        // called method takes care of not found exception
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

    public Address updateAddress(Long id, Address newAddress){
        // called method takes care of not found exception
        Address address = getAddressById(id);
        // check for possible null fields that aren't nullable
        checkAddressForNullFields(newAddress);

        // no need to do anything if it's the same as in db
        if(newAddress.equals(address)) return address;
        // check if address is a duplicate
        if(isAddressADuplicate(newAddress)) throw new AlreadyExistsException(ADDRESS);

        address.setCountry(newAddress.getCountry());
        address.setRegion(newAddress.getRegion());
        address.setZipCode(newAddress.getZipCode());
        address.setCity(newAddress.getCity());
        address.setStreet(newAddress.getStreet());
        address.setBuildingNumber(newAddress.getBuildingNumber());
        address.setSecondaryNumber(newAddress.getSecondaryNumber());

        return addressRepository.save(address);
    }

    // simply checks for fields that cant be null, if they are, it throws an exception
    public void checkAddressForNullFields(Address address){
        if(address.getCountry() == null
                || address.getZipCode() == null
                || address.getCity() == null
                || address.getBuildingNumber() == null)
        {
            throw new HasNullFieldsException(ADDRESS);
        }
    }

    public boolean isAddressADuplicate(Address address){
        List<Address> addresses = getAddresses();

        for(Address dbAddress : addresses){
            if(dbAddress.equals(address)){
                return true;
            }
        }

        return false;
    }
}
