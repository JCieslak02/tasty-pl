package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.MealIsFromDifferentRestaurantException;
import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.payload.request.MealRequest;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.payload.response.MealResponse;
import com.jcieslak.tastypl.repository.MealRepository;
import com.jcieslak.tastypl.mapper.MealMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantService restaurantService;
    private final MealMapper mealMapper;
    private static final String MEAL = "meal";

    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    public Meal getMealByIdOrThrowExc(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEAL, id));
    }

    public List<MealResponse> getAllMealsByRestaurantId(Long restaurantId){
        //called method checks whether the provided id is valid, throws notFound exc otherwise
        restaurantService.getRestaurantByIdOrThrowExc(restaurantId);
        return mealRepository.findAllByRestaurantId(restaurantId).stream()
                .map(mealMapper::toResponse)
                .toList();
    }

    public MealResponse createMeal(MealRequest mealRequest, Long restaurantId){
        Meal meal = mealMapper.toEntity(mealRequest);
        Restaurant restaurant = restaurantService.getRestaurantByIdOrThrowExc(restaurantId);
        meal.setRestaurant(restaurant);

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        // only a restaurant owner can add a meal to their place
        if(!restaurantService.isPrincipalOwnerOfRestaurant(restaurant)){
            throw new PrincipalIsNotAnOwnerException();
        }

        mealRepository.save(meal);

        return mealMapper.toResponse(meal);
    }

    public MealResponse updateMeal(Long mealId, Long restaurantId, MealRequest mealRequest){
        Meal meal = getMealByIdOrThrowExc(mealId);
        Meal newMeal = mealMapper.toEntity(mealRequest);
        Restaurant restaurant = restaurantService.getRestaurantByIdOrThrowExc(restaurantId);
        // this field is set to simplify the isMealADuplicate method, otherwise I'd have to implement this method twice or override it
        // , so imo it's the best way
        newMeal.setRestaurant(meal.getRestaurant());

        // no duplicates will get through
        if(isMealADuplicate(newMeal)) throw new AlreadyExistsException(MEAL);

        // meal has to be from the restaurant, and the user has to be the owner
        validateMealRestaurantUser(meal, restaurant);

        meal.setType(newMeal.getType());
        meal.setName(newMeal.getName());
        meal.setPrice(newMeal.getPrice());

        mealRepository.save(meal);

        return mealMapper.toResponse(meal);
    }

    public void deleteMeal(Long restaurantId, Long mealId){
        Meal meal = getMealByIdOrThrowExc(mealId);
        Restaurant restaurant = restaurantService.getRestaurantByIdOrThrowExc(restaurantId);

        // meal has to be from the restaurant, and the user has to be the owner
        validateMealRestaurantUser(meal, restaurant);

        mealRepository.delete(meal);
    }

    public boolean isMealADuplicate(Meal meal){
        return getAllMeals().stream()
                .anyMatch(m -> m.equals(meal));
    }

    //this method throws exc if a meal is from another restaurant or a user is not an owner of this restaurant
    public void validateMealRestaurantUser(Meal meal, Restaurant restaurant){
        // meal has to be from the same restaurant
        if(!meal.getRestaurant().equals(restaurant)){
            throw new MealIsFromDifferentRestaurantException();
        }

        // only an owner of the restaurant can deal with their meal
        if(!restaurantService.isPrincipalOwnerOfRestaurant(restaurant)){
            throw new PrincipalIsNotAnOwnerException();
        }
    }
}
