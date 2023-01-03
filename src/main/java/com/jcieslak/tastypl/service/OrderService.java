package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.enums.OrderStatus;
import com.jcieslak.tastypl.model.Meal;
import com.jcieslak.tastypl.model.Order;
import com.jcieslak.tastypl.model.OrderMealQuantity;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.OrderRequest;
import com.jcieslak.tastypl.payload.response.MealQuantityResponse;
import com.jcieslak.tastypl.payload.response.OrderResponse;
import com.jcieslak.tastypl.repository.MealRepository;
import com.jcieslak.tastypl.repository.OrderRepository;
import com.jcieslak.tastypl.security.config.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper modelMapper;
    private final MealRepository mealRepository;
    private final MealService mealService;

    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal){
        User user = userDetailsService.loadUserByUsername(principal.getName());
        Order order = modelMapper.map(orderRequest, Order.class);

        List<OrderMealQuantity> orderMealQuantityList = orderRequest
                .getOrderMealQuantities().stream()
                .map(o -> {
                    OrderMealQuantity orderMealQuantity = modelMapper.map(o, OrderMealQuantity.class);
                    orderMealQuantity.setMeal(mealService.getMealByIdOrThrowExc(o.getMealId()));
                    orderMealQuantity.setOrder(order);
                    return orderMealQuantity;
                })
                .toList();

        BigDecimal total = orderMealQuantityList.stream()
                .map(orderMealQuantity -> {
                    Meal meal = orderMealQuantity.getMeal();
                    int quantity = orderMealQuantity.getQuantity();
                    BigDecimal price = meal.getPrice();
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setMeals(orderMealQuantityList);
        order.setTotal(total);
        order.setDateTime(LocalDateTime.now());

        orderRepository.save(order);

        List<MealQuantityResponse> mealQuantityResponseList = orderMealQuantityList.stream()
                .map(o -> {
                    MealQuantityResponse m = new MealQuantityResponse();
                    m.setMealId(o.getMeal().getId());
                    m.setName(o.getMeal().getName());
                    m.setType(o.getMeal().getType());
                    m.setPrice(o.getMeal().getPrice());
                    return m;
                })
                .toList();

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setMeals(mealQuantityResponseList);
        return orderResponse;
    }
}
