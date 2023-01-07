package com.jcieslak.tastypl.mapper;

import com.jcieslak.tastypl.model.Order;
import com.jcieslak.tastypl.payload.request.OrderRequest;
import com.jcieslak.tastypl.payload.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;
    private final MealQuantityListMapper mealQuantityListMapper;

    public OrderResponse toResponse(Order order){
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setMealQuantityResponseList(mealQuantityListMapper.toResponse(order.getOrderMealQuantityList()));
        return orderResponse;
    }

    public Order toEntity(OrderRequest orderRequest){
        return modelMapper.map(orderRequest, Order.class);
    }
}
