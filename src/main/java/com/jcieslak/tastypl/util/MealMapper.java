package com.jcieslak.tastypl.util;

import com.jcieslak.tastypl.dto.MealDTO;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {
    private final ModelMapper modelMapper;
    private RestaurantService restaurantService;

    public MealMapper() {
        modelMapper = new ModelMapper();
        modelMapper.createTypeMap(MealDTO.class, Meal.class)
                .addMappings(mapper -> mapper.skip(Meal::setRestaurant));
    }

    public MealDTO toDTO(Meal meal) {
        return modelMapper.map(meal, MealDTO.class);
    }

    public Meal toEntity(MealDTO mealDTO) {
        Meal meal = modelMapper.map(mealDTO, Meal.class);
        meal.setRestaurant(restaurantService.getRestaurantById(mealDTO.getRestaurantId()));
        return meal;
    }
}


