package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.payload.request.MealRequest;
import com.jcieslak.tastypl.exception.AlreadyExistsException;
import com.jcieslak.tastypl.exception.HasNullFieldsException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.payload.response.MealResponse;
import com.jcieslak.tastypl.repository.MealRepository;
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

    public List<MealResponse> getAllMeals(){
        List<Meal> meals = mealRepository.findAll();

        return meals.stream()
                .map(mealMapper::toResponse)
                .toList();
    }

    public MealResponse getMealById(Long id){
        return mealMapper
                .toResponse(getMealByIdOrThrowExc(id));
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

        //throws hasNullFields exc if there are any null fields
        checkForNullFields(meal);

        if(isMealADuplicate(meal)) throw new AlreadyExistsException(MEAL);

        mealRepository.save(meal);

        return mealMapper.toResponse(meal);
    }

    public MealResponse updateMeal(Long id, MealRequest mealRequest){
        Meal meal = getMealByIdOrThrowExc(id);
        Meal newMeal = mealMapper.toEntity(mealRequest);
        checkForNullFields(newMeal);

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
    //TODO: type mismatch
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
