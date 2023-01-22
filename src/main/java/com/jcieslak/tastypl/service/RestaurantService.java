package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.RestaurantRequest;
import com.jcieslak.tastypl.payload.response.RestaurantResponse;
import com.jcieslak.tastypl.repository.RestaurantRepository;
import com.jcieslak.tastypl.mapper.RestaurantMapper;
import com.jcieslak.tastypl.security.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private static final String RESTAURANT = "restaurant";
    private final AddressService addressService;
    private final RestaurantMapper restaurantMapper;
    private final AuthService authService;

    public List<RestaurantResponse> getRestaurants(String city, String type, String sort, String direction){
        Sort order = Sort.by(sort, Direction.);
        List<Restaurant> restaurants = restaurantRepository.findByAddressCity(city);
        return restaurants.stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    public Restaurant getRestaurantByIdOrThrowExc(Long id){
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESTAURANT, id));
    }

    public RestaurantResponse getRestaurantByIdResponse(Long id){
        return restaurantMapper.toResponse(getRestaurantByIdOrThrowExc(id));
    }

    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest){
        //setting owner to currently logged user, controller ensures that only users with ROLE_RESTAURANT_OWNER can access this method
        User owner = authService.getPrincipal();
        Restaurant restaurant = restaurantMapper.toEntity(restaurantRequest);
        restaurant.setOwner(owner);
        restaurant.setRating(0);
        restaurant.setReviewCount(0);

        //this block takes care of duplicate data that can only belong to one restaurant (db unique const.) or null fields in address
        try{
            restaurantRepository.save(restaurant);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Provided restaurant has non unique fields or address has null fields");
        }
        return restaurantMapper.toResponse(restaurant);
    }

    public void deleteRestaurant(Long id){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantByIdOrThrowExc(id);

        //only an owner can delete a restaurant
        //TODO: fix no message
        if(!isPrincipalOwnerOfRestaurant(restaurant)){
            throw new PrincipalIsNotAnOwnerException();
        }

        restaurantRepository.delete(restaurant);

    }

    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest restaurantRequest){
        // throws notFound exception if provided id is wrong
        Restaurant restaurant = getRestaurantByIdOrThrowExc(id);

        //only an owner can update their restaurant
        //TODO: fix no message
        if(!isPrincipalOwnerOfRestaurant(restaurant)){
            throw new PrincipalIsNotAnOwnerException();
        }

        Restaurant newRestaurant = restaurantMapper.toEntity(restaurantRequest);

        if(isRestaurantADuplicate(newRestaurant)){
            throw new AlreadyExistsException(RESTAURANT);
        }

        //address gets updated in db, called method handles validation
        addressService.updateAddress(restaurant.getAddress().getId(), newRestaurant.getAddress());

        restaurant.setName(newRestaurant.getName());
        restaurant.setType(newRestaurant.getType());
        restaurant.setPhoneNumber(newRestaurant.getPhoneNumber());
        restaurant.setEmail(newRestaurant.getEmail());

        // due to unique constraints in db, it can throw an exc for some fields
        try{
            restaurantRepository.save(restaurant);
        }
        catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Provided restaurant has non unique fields or address has null fields");
        }
        return restaurantMapper.toResponse(restaurant);
    }

    // operationNum:
    // 1: add review
    // 2: delete review
    public void updateReviewScoreAndReviewCount(Long restaurantId, int stars, int operationNum){
        Restaurant restaurant = getRestaurantByIdOrThrowExc(restaurantId);
        int reviewCount = restaurant.getReviewCount();
        double allReviewsAveraged = restaurant.getRating()*reviewCount;

        if(operationNum == 1) reviewCount++;
        else if(operationNum == 2) reviewCount--;

        double newScore = (allReviewsAveraged + stars) / reviewCount;

        restaurant.setRating(newScore);
        restaurant.setReviewCount(reviewCount);
    }

    public boolean isRestaurantADuplicate(Restaurant restaurant){
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .anyMatch(r -> r.equals(restaurant));
    }

    public boolean isPrincipalOwnerOfRestaurant(Restaurant restaurant){
        User user = authService.getPrincipal();
        return Objects.equals(restaurant.getOwner(), user);
    }
}
