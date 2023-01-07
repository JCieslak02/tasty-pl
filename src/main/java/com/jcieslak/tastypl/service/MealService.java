package com.jcieslak.tastypl.service;

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

    public MealResponse getMealById(Long id){
        return mealMapper.toResponse(getMealByIdOrThrowExc(id));
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
        meal.setRestaurant(restaurantService.getRestaurantByIdOrThrowExc(restaurantId));

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        mealRepository.save(meal);

        return mealMapper.toResponse(meal);
    }

    public MealResponse updateMeal(Long id, MealRequest mealRequest){
        Meal meal = getMealByIdOrThrowExc(id);
        Meal newMeal = mealMapper.toEntity(mealRequest);

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        meal.setType(newMeal.getType());
        meal.setName(newMeal.getName());
        meal.setPrice(newMeal.getPrice());

        mealRepository.save(meal);

        return mealMapper.toResponse(meal);
    }

    public void deleteMeal(Long id){
        Meal meal = getMealByIdOrThrowExc(id);
        mealRepository.delete(meal);
    }

    public boolean isMealADuplicate(Meal meal){
        return getAllMeals().stream()
                .anyMatch(m -> m.equals(meal));
    }
}
