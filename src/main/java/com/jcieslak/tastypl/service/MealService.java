package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.dto.MealDTO;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.repository.MealRepository;
import com.jcieslak.tastypl.service.RestaurantService;
import com.jcieslak.tastypl.util.MealMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MEAL, id));
    }

    public List<MealDTO> getAllMealsByRestaurantId(Long restaurantId){
        //called method checks whether the provided id is valid, throws notFound exc otherwise
        restaurantService.getRestaurantById(restaurantId);
        return mealRepository.findAllByRestaurantId(restaurantId).stream()
                .map(mealMapper::toDTO)
                .toList();
    }

    public MealDTO createMeal(MealDTO mealDTO){
        Meal meal = mealMapper.toEntity(mealDTO);
        //throws hasNullFields exc if there are any null fields
        checkForNullFields(meal);

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        mealRepository.save(meal);

        return mealMapper.toDTO(meal);
    }

    public Meal updateMeal(Long id, Meal newMeal){
        Meal meal = getMealById(id);

        checkForNullFields(newMeal);
        if(newMeal.equals(meal)) return meal;
        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        meal.setType(newMeal.getType());
        meal.setName(newMeal.getName());
        meal.setPrice(newMeal.getPrice());
        meal.setRestaurant(newMeal.getRestaurant());

        return meal;
    }

    public void deleteMeal(Long id){
        Meal meal = getMealById(id);
        mealRepository.delete(meal);
    }
    public boolean isMealADuplicate(Meal meal){
        return getAllMeals().stream()
                .anyMatch(m -> m.equals(meal));
    }

    public void checkForNullFields(Meal meal) {
        List<String> nullFields = Stream.of(meal.getType(), meal.getPrice().toString(), meal.getName())
                .filter(Objects::isNull)
                .toList();
        if (!nullFields.isEmpty()) {
            throw new HasNullFieldsException(MEAL);
        }
    }
}
