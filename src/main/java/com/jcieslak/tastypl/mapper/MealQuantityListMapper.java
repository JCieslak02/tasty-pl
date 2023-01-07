package com.jcieslak.tastypl.mapper;

import com.jcieslak.tastypl.model.Order;
import com.jcieslak.tastypl.model.OrderMealQuantity;
import com.jcieslak.tastypl.payload.request.MealQuantityRequest;
import com.jcieslak.tastypl.payload.response.MealQuantityResponse;
import com.jcieslak.tastypl.service.MealService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MealQuantityListMapper {
    private final ModelMapper modelMapper;
    private final MealService mealService;

    public List<MealQuantityResponse> toResponse(List<OrderMealQuantity> orderMealQuantityList){
        return orderMealQuantityList.stream()
                .map(o -> modelMapper.map(o, MealQuantityResponse.class))
                .toList();
    }

    public List<OrderMealQuantity> fromRequestToEntity(List<MealQuantityRequest> mealQuantityRequestList, Order order){
        return mealQuantityRequestList.stream()
                .map(m -> {
                    OrderMealQuantity orderMealQuantity = modelMapper.map(m, OrderMealQuantity.class);
                    orderMealQuantity.setMeal(mealService.getMealByIdOrThrowExc(m.getMealId()));
                    orderMealQuantity.setOrder(order);
                    return orderMealQuantity;
                }
                )
                .toList();
    }
}
