package com.jcieslak.tastypl.restaurant;

import com.jcieslak.tastypl.address.Address;
import com.jcieslak.tastypl.address.AddressService;
import com.jcieslak.tastypl.contact.Contact;
import com.jcieslak.tastypl.contact.ContactService;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final ContactService contactService;
    private static final String RESTAURANT = "restaurant";

    public List<Restaurant> getRestaurants(){
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id){
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESTAURANT, id));
    }

    public Restaurant createRestaurant(Restaurant restaurant){
        // checks whether provided restaurant has null fields, if so, throws a hasNullFields exception
        checkForNullFields(restaurant);
        if(isRestaurantADuplicate(restaurant)) throw new AlreadyExistsException(RESTAURANT);

        // these two calls to external services also validate given values for uniqueness and null fields
        addressService.createAddress(restaurant.getAddress());
        contactService.createContact(restaurant.getContact());

        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantById(id);

        // checks whether provided restaurant has null fields, if so, throws a hasNullFields exception
        checkForNullFields(newRestaurant);

        // checks provided new restaurant whether it is a duplicate or not
        if(isRestaurantADuplicate(newRestaurant)) throw new AlreadyExistsException(RESTAURANT);

        contactService.updateContact(restaurant.getContact().getId(), newRestaurant.getContact());
        addressService.updateAddress(restaurant.getAddress().getId(), newRestaurant.getAddress());

        restaurant.setType(newRestaurant.getType());
        restaurant.setName(newRestaurant.getName());

        return restaurantRepository.save(restaurant);
    }

    public Contact updateRestaurantContact(Long restaurantId, Contact contact){
        Restaurant restaurant = getRestaurantById(restaurantId);

        // this call performs all validation necessary
        return contactService.updateContact(restaurant.getContact().getId(), contact);
    }

    public Address updateRestaurantAddress(Long restaurantId, Address address){
        Restaurant restaurant = getRestaurantById(restaurantId);

        // this call performs all validation necessary
        return addressService.updateAddress(restaurant.getAddress().getId(), address);
    }

    public boolean isRestaurantADuplicate(Restaurant restaurant){
        List<Restaurant> restaurants = getRestaurants();

        for(Restaurant dbRestaurant : restaurants){
            if(dbRestaurant.equals(restaurant)) return true;
        }

        return false;
    }

    public void checkForNullFields(Restaurant restaurant){
        if(restaurant.getName() == null
            || restaurant.getType() == null
            || restaurant.getAddress() == null
            || restaurant.getContact() == null){
                throw new HasNullFieldsException(RESTAURANT);
        }
    }
}
