package com.jcieslak.tastypl.meal;

import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.restaurant.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantService restaurantService;
    private static final String MEAL = "meal";

    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEAL, id));
    }

    public List<Meal> getAllMealsByRestaurantId(Long id){
        //called method checks whether the provided id is valid, throws notFound exc otherwise
        restaurantService.checkIfRestaurantExistsById(id);
        return mealRepository.findAllByRestaurantId(id);
    }

    public Meal createMeal(Meal meal){
        //throws hasNullFields exc if there are any null fields
        checkForNullFields(meal);

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        return mealRepository.save(meal);
    }

    public Meal updateMeal(Long id, Meal newMeal){
        Meal meal = getMealById(id);

        checkForNullFields(newMeal);
        if(newMeal.equals(meal)) return meal;
        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        meal.setType(newMeal.getType());
        meal.setName(newMeal.getName());
        meal.setPrice(newMeal.getPrice());
        meal.setRestaurantId(newMeal.getRestaurantId());

        return meal;
    }

    public void deleteMeal(Long id){
        Meal meal = getMealById(id);
        mealRepository.delete(meal);
    }
    public boolean isMealADuplicate(Meal meal){
        List<Meal> meals = getAllMeals();

        for(Meal dbMeal : meals){
            if(dbMeal.equals(meal)) return true;
        }

        return false;
    }

    public void checkForNullFields(Meal meal){
        if(meal.getType() == null ||
            meal.getPrice() == null ||
            meal.getName() == null ||
            meal.getRestaurantId() == null){
                throw new HasNullFieldsException(MEAL);
        }
    }
}
