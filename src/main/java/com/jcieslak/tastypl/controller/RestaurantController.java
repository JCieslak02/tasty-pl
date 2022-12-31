package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.dto.MealDTO;
import com.jcieslak.tastypl.service.MealService;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MealService mealService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") Long id){
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MealDTO>> getRestaurantMenuById(@PathVariable("id") Long id){
        List<MealDTO> menu = mealService.getAllMealsByRestaurantId(id);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant){
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable("id") Long id, @RequestBody Restaurant newRestaurant){
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, newRestaurant);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id){
        restaurantService.deleteRestaurant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/meals")
    public ResponseEntity<MealDTO> createMeal(@PathVariable("id") Long restaurantId, @RequestBody MealDTO mealDTO){
        mealDTO.setRestaurantId(restaurantId);
        mealService.createMeal(mealDTO);
        return new ResponseEntity<>(mealDTO, HttpStatus.CREATED);
    }
}
