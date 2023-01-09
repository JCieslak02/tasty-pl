package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.model.Address;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.repository.AddressRepository;
import com.jcieslak.tastypl.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AddressService {
    public static final String ADDRESS = "address";
    private final AddressRepository addressRepository;
    private final RestaurantRepository restaurantRepository;

    public Address getAddressById(Long id){
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ADDRESS, id));
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    // this method is used just in RestaurantService to update restaurant's address
    public void updateAddress(Long id, Address newAddress){
        // called method takes care of not found exception
        Address address = getAddressById(id);

        // no need to do anything if it's the same as in db
        if(newAddress.equals(address)) return;

        // check if address is owned by another restaurant, nothing wrong will happen if it's owned by restaurant that's being updated,
        // since it's handled in a line above it
        if(isAddressOwnedByAnotherRestaurant(newAddress)) throw new AlreadyExistsException(ADDRESS);

        address.setCountry(newAddress.getCountry());
        address.setRegion(newAddress.getRegion());
        address.setZipCode(newAddress.getZipCode());
        address.setCity(newAddress.getCity());
        address.setStreet(newAddress.getStreet());
        address.setBuildingNumber(newAddress.getBuildingNumber());
        address.setSecondaryNumber(newAddress.getSecondaryNumber());

        addressRepository.save(address);
    }

    // method used in updateAddress
    public boolean isAddressOwnedByAnotherRestaurant(Address address){
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream()
                .anyMatch(r -> address.equals(r.getAddress()));
    }

    // method used in OrderService to ensure no duplicate addresses will be saved in db
    public Address getDuplicateAddressOrProvided(Address address){
        List<Address> addressList = getAllAddresses();

        return addressList.stream()
                .filter(dbAddress -> dbAddress.equals(address))
                .findFirst()
                .orElse(address);
    }
}
