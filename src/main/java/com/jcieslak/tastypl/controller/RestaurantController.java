package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.request.MealRequest;
import com.jcieslak.tastypl.payload.request.RestaurantRequest;
import com.jcieslak.tastypl.payload.response.MealResponse;
import com.jcieslak.tastypl.payload.response.RestaurantResponse;
import com.jcieslak.tastypl.payload.response.ReviewResponse;
import com.jcieslak.tastypl.service.MealService;
import com.jcieslak.tastypl.service.RestaurantService;
import com.jcieslak.tastypl.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MealService mealService;
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(){
        List<RestaurantResponse> restaurants = restaurantService.getAllRestaurantsResponse();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long restaurantId){
        RestaurantResponse restaurant = restaurantService.getRestaurantByIdResponse(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}/meals")
    public ResponseEntity<List<MealResponse>> getRestaurantMealsById(@PathVariable Long restaurantId){
        List<MealResponse> menu = mealService.getAllMealsByRestaurantId(restaurantId);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest){
        RestaurantResponse createdRestaurant = restaurantService.createRestaurant(restaurantRequest);
        return new ResponseEntity<>(createdRestaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantRequest restaurantRequest){
        RestaurantResponse updatedRestaurant = restaurantService.updateRestaurant(restaurantId, restaurantRequest);
        return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId){
        restaurantService.deleteRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{restaurantId}/meals")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<MealResponse> createMealInRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody MealRequest mealRequest){
        MealResponse mealResponse = mealService.createMeal(mealRequest, restaurantId);
        return new ResponseEntity<>(mealResponse, HttpStatus.CREATED);
    }
    @PostMapping("/{restaurantId}/meals/{mealId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<Void> deleteMealInRestaurant(@PathVariable("restaurantId") Long restaurantId, @PathVariable("mealId") Long mealId){
        mealService.deleteMeal(restaurantId, mealId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/meals/{mealId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<MealResponse> updateMealInRestaurant(@PathVariable("restaurantId") Long restaurantId, @PathVariable("mealId") Long mealId,
                                                               @Valid @RequestBody MealRequest mealRequest){
        MealResponse mealResponse = mealService.updateMeal(mealId, restaurantId, mealRequest);
        return new ResponseEntity<>(mealResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsByRestaurantId(@PathVariable Long restaurantId){
        List<ReviewResponse> reviewResponseList = reviewService.getAllReviewsByRestaurantId(restaurantId);
        return new ResponseEntity<>(reviewResponseList, HttpStatus.OK);
    }
}
