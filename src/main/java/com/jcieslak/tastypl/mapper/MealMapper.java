package com.jcieslak.tastypl.mapper;

import com.jcieslak.tastypl.payload.request.MealRequest;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.payload.response.MealResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {
    private final ModelMapper modelMapper;

    public MealMapper() {
        modelMapper = new ModelMapper();
    }

    public Meal toEntity(MealRequest mealRequest) {
        return modelMapper.map(mealRequest, Meal.class);
    }

    public MealResponse toResponse(Meal meal){
        return modelMapper.map(meal, MealResponse.class);
    }
}


