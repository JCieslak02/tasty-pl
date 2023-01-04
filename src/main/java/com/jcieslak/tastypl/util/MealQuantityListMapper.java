package com.jcieslak.tastypl.util;

import com.jcieslak.tastypl.model.OrderMealQuantity;
import com.jcieslak.tastypl.payload.response.MealQuantityResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MealQuantityListMapper {
    private final ModelMapper modelMapper;

    public MealQuantityListMapper(){
        modelMapper = new ModelMapper();
    }

    public List<MealQuantityResponse> toResponse(List<OrderMealQuantity> orderMealQuantityList){
        return orderMealQuantityList.stream()
                .map(o -> modelMapper.map(o, MealQuantityResponse.class))
                .toList();
    }
}
