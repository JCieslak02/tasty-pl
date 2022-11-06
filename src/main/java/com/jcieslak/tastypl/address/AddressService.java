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
    //this constant is a dum dum solution, I'll change it soon
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
        boolean isDuplicate = isAddressADuplicate(address);

        if(isDuplicate) throw new AlreadyExistsException(ADDRESS);

        return addressRepository.save(address);
    }

    public void deleteAddress(Long id){
        //called method takes care of not found exception
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

    public Address updateAddress(Long id, Address newAddress){
        //called method takes care of not found exception
        Address address = getAddressById(id);

        if(newAddress.getCountry() == null
                || newAddress.getZipCode() == null
                || newAddress.getCity() == null
                || newAddress.getBuildingNumber() == null)
        {
            throw new HasNullFieldsException(ADDRESS);
        }

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

    private boolean isAddressADuplicate(Address address){
        List<Address> addresses = getAddresses();

        for(Address dbAddress : addresses){
            if(dbAddress.equals(address)){
                return true;
            }
        }

        return false;
    }
}
