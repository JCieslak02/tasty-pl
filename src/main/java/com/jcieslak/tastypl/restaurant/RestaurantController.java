package com.jcieslak.tastypl.restaurant;

import com.jcieslak.tastypl.address.Address;
import com.jcieslak.tastypl.contact.Contact;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<Restaurant> getAllRestaurants(){
        return restaurantService.getRestaurants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") Long id){
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant){
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable("id") Long id, @RequestBody Restaurant newRestaurant){
        Restaurant restaurant = restaurantService.updateRestaurant(id, newRestaurant);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id){
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/contacts")
    public ResponseEntity<Contact> updateRestaurantContact(@PathVariable Long id, @RequestBody Contact contact){
        Contact updatedContact = restaurantService.updateRestaurantContact(id, contact);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/addresses")
    public ResponseEntity<Address> updateRestaurantAddress(@PathVariable Long id, @RequestBody Address address){
        Address updatedAddress = restaurantService.updateRestaurantAddress(id, address);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
