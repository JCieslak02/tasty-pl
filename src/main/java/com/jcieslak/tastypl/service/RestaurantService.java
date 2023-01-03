package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.RestaurantRequest;
import com.jcieslak.tastypl.payload.response.RestaurantResponse;
import com.jcieslak.tastypl.repository.RestaurantRepository;
import com.jcieslak.tastypl.security.config.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private static final String RESTAURANT = "restaurant";
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final AddressService addressService;

    public List<RestaurantResponse> getAllRestaurants(){
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(r -> modelMapper.map(r, RestaurantResponse.class))
                .toList();
    }

    public Restaurant getRestaurantByIdOrThrowExc(Long id){
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESTAURANT, id));
    }

    public RestaurantResponse getRestaurantByIdForController(Long id){
        return modelMapper.map(getRestaurantByIdOrThrowExc(id), RestaurantResponse.class);
    }

    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest, Principal principal){
        //setting owner to currently logged user, controller ensures that only users with ROLE_RESTAURANT_OWNER can access this method
        User owner = userDetailsService.loadUserByUsername(principal.getName());
        Restaurant restaurant = modelMapper.map(restaurantRequest, Restaurant.class);
        restaurant.setOwner(owner);

        //this block takes care of duplicate data that can only belong to one restaurant (db unique const.) or null fields in address
        try{
            restaurantRepository.save(restaurant);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Provided restaurant has non unique fields or address has null fields");
        }
        return modelMapper.map(restaurant, RestaurantResponse.class);
    }

    public void deleteRestaurant(Long id, Principal principal){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantByIdOrThrowExc(id);

        //only an owner can delete a restaurant
        //TODO: fix no message
        if(!isPrincipalOwnerOfRestaurant(restaurant, principal)){
            throw new PrincipalIsNotAnOwnerException("User isn't the owner of the restaurant");
        }

        restaurantRepository.delete(restaurant);

    }

    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest restaurantRequest, Principal principal){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantByIdOrThrowExc(id);

        //only an owner can update their restaurant
        //TODO: fix no message
        if(!isPrincipalOwnerOfRestaurant(restaurant, principal)){
            throw new PrincipalIsNotAnOwnerException("User isn't the owner of the restaurant");
        }

        Restaurant newRestaurant = modelMapper.map(restaurantRequest, Restaurant.class);

        if(isRestaurantADuplicate(newRestaurant)){
            throw new AlreadyExistsException(RESTAURANT);
        }

        //address gets updated in db, called method handles validation
        addressService.updateAddress(restaurant.getAddress().getId(), newRestaurant.getAddress());

        restaurant.setName(newRestaurant.getName());
        restaurant.setType(newRestaurant.getType());
        restaurant.setPhoneNumber(newRestaurant.getPhoneNumber());
        restaurant.setEmail(newRestaurant.getEmail());

        try{
            restaurantRepository.save(restaurant);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Provided restaurant has non unique fields or address has null fields");
        }
        return modelMapper.map(restaurant, RestaurantResponse.class);
    }
    public boolean isRestaurantADuplicate(Restaurant restaurant){
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .anyMatch(r -> r.equals(restaurant));
    }

    public boolean isPrincipalOwnerOfRestaurant(Restaurant restaurant, Principal principal){
        return Objects.equals(restaurant.getOwner().getUsername(), principal.getName());
    }
}
