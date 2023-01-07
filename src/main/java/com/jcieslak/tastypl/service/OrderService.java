package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.enums.OrderStatus;
import com.jcieslak.tastypl.exception.EnumConstantNotPresentException;
import com.jcieslak.tastypl.exception.MealIsFromDifferentRestaurantException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.model.*;
import com.jcieslak.tastypl.payload.request.MealQuantityRequest;
import com.jcieslak.tastypl.payload.request.OrderRequest;
import com.jcieslak.tastypl.payload.request.OrderStatusUpdateRequest;
import com.jcieslak.tastypl.payload.response.MealQuantityResponse;
import com.jcieslak.tastypl.payload.response.OrderResponse;
import com.jcieslak.tastypl.repository.OrderRepository;
import com.jcieslak.tastypl.util.MealQuantityListMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final MealService mealService;
    private final AddressService addressService;
    private final MealQuantityListMapper mealQuantityListMapper;
    private final RestaurantService restaurantService;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Order getOrderByIdOrThrowExc(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("order", id));
    }
    public OrderResponse createOrder(OrderRequest orderRequest){
        // this block of code ensures that there won't be any address duplicates in db - if it's present in db, it'll be set as provided address
        // in order request
        Address address = addressService.getDuplicateAddressOrProvided(orderRequest.getAddress());
        orderRequest.setAddress(address);
        Order order = modelMapper.map(orderRequest, Order.class);

        // creating a list of OrderMealQuantity for later counting total price and saving it to db via cascade
        // by setting order it as a field in order entity
        List<MealQuantityRequest> mealQuantityRequestList = orderRequest.getMealQuantityRequestList();

        List<OrderMealQuantity> orderMealQuantityList = mealQuantityRequestList.stream()
                .map(o -> {
                    OrderMealQuantity orderMealQuantity = modelMapper.map(o, OrderMealQuantity.class);
                    orderMealQuantity.setMeal(mealService.getMealByIdOrThrowExc(o.getMealId()));
                    orderMealQuantity.setOrder(order);
                    return orderMealQuantity;
                })
                .toList();

        // check whether all meals are from the same restaurant, throw exception if not
        validateMealsRestaurant(orderMealQuantityList, orderRequest);

        // calculate total price
        BigDecimal total = calculateTotal(orderMealQuantityList);

        // property settings to save order in db together with address and OrderMealQuantity
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderMealQuantityList(orderMealQuantityList);
        order.setTotal(total);
        order.setDateTime(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // lines below convert stuff to create OrderResponse return object
        List<MealQuantityResponse> mealQuantityResponseList = mealQuantityListMapper.toResponse(orderMealQuantityList);

        OrderResponse orderResponse = modelMapper.map(savedOrder, OrderResponse.class);
        orderResponse.setMealQuantityResponseList(mealQuantityResponseList);
        logger.info("Order created successfully");
        return orderResponse;
    }

    public List<OrderResponse> getAllOrdersByRestaurantId(Long restaurantId){
        List<Order> orderList = orderRepository.findAllByRestaurantId(restaurantId);

        return getOrderResponses(orderList);
    }

    public List<OrderResponse> getAllOrdersByUserId(Long userId){
        List<Order> orderList = orderRepository.findAllByUserId(userId);

        return getOrderResponses(orderList);
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = getOrderByIdOrThrowExc(orderId);
        Restaurant restaurant = order.getRestaurant();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!restaurantService.isPrincipalOwnerOfRestaurant(restaurant, user)) {
            throw new PrincipalIsNotAnOwnerException("Why didnt I make this message easier?");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getOrderStatus().toUpperCase());
        } catch (Exception e) {
            throw new EnumConstantNotPresentException(request.getOrderStatus());
        }


        logger.info("after if statment");
        order.setOrderStatus(newStatus);
        logger.info("set status");
        return modelMapper.map(order, OrderResponse.class);
    }

    //method used internally to create a list of OrderResponses from a list of Orders from repo
    public List<OrderResponse> getOrderResponses(List<Order> orderList){
        return orderList.stream()
                .map(o -> {
                    OrderResponse orderResponse = modelMapper.map(o, OrderResponse.class);
                    orderResponse.setMealQuantityResponseList(mealQuantityListMapper.toResponse(o.getOrderMealQuantityList()));
                    return orderResponse;
                })
                .toList();
    }

    //method checks if all meals are from the same restaurant, throws exception if not
    public void validateMealsRestaurant(List<OrderMealQuantity> orderMealQuantityList, OrderRequest orderRequest){
        boolean isAnyMealFromDifferentRestaurant = orderMealQuantityList.stream()
                .anyMatch(m -> !Objects.equals(m.getMeal().getRestaurant().getId(), orderRequest.getRestaurantId()));

        if(isAnyMealFromDifferentRestaurant){
            throw new MealIsFromDifferentRestaurantException();
        }
    }

    //method calculates total price for an order
    public BigDecimal calculateTotal(List<OrderMealQuantity> orderMealQuantityList){
        return orderMealQuantityList.stream()
                .map(o -> {
                    Meal meal = o.getMeal();
                    int quantity = o.getQuantity();
                    BigDecimal price = meal.getPrice();
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
