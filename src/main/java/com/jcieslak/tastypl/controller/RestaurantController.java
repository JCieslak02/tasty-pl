package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.request.MealRequest;
import com.jcieslak.tastypl.payload.request.RestaurantRequest;
import com.jcieslak.tastypl.payload.response.MealResponse;
import com.jcieslak.tastypl.payload.response.RestaurantResponse;
import com.jcieslak.tastypl.service.MealService;
import com.jcieslak.tastypl.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MealService mealService;

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(){
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable("id") Long id){
        RestaurantResponse restaurant = restaurantService.getRestaurantByIdForController(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MealResponse>> getRestaurantMenuById(@PathVariable("id") Long id){
        List<MealResponse> menu = mealService.getAllMealsByRestaurantId(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest restaurantRequest, Principal principal){
        RestaurantResponse createdRestaurant = restaurantService.createRestaurant(restaurantRequest, principal);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable("id") Long id, @RequestBody RestaurantRequest restaurantRequest, Principal principal){
        RestaurantResponse updatedRestaurant = restaurantService.updateRestaurant(id, restaurantRequest, principal);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id, Principal principal){
        restaurantService.deleteRestaurant(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/meals")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<MealRequest> createMeal(@PathVariable("id") Long restaurantId, @RequestBody MealRequest mealRequest){
        mealService.createMeal(mealRequest, restaurantId);
        return new ResponseEntity<>(mealRequest, HttpStatus.CREATED);
    }
}
