package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.enums.OrderStatus;
import com.jcieslak.tastypl.enums.UserRole;
import com.jcieslak.tastypl.exception.EnumConstantNotPresentException;
import com.jcieslak.tastypl.exception.MealIsFromDifferentRestaurantException;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.model.*;
import com.jcieslak.tastypl.payload.request.MealQuantityRequest;
import com.jcieslak.tastypl.payload.request.OrderRequest;
import com.jcieslak.tastypl.payload.request.OrderStatusUpdateRequest;
import com.jcieslak.tastypl.payload.response.OrderResponse;
import com.jcieslak.tastypl.repository.OrderRepository;
import com.jcieslak.tastypl.mapper.MealQuantityListMapper;
import com.jcieslak.tastypl.mapper.OrderMapper;
import lombok.AllArgsConstructor;
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
    private final AddressService addressService;
    private final MealQuantityListMapper mealQuantityListMapper;
    private final RestaurantService restaurantService;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderMapper orderMapper;

    public Order getOrderByIdOrThrowExc(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("order", id));
    }
    public OrderResponse createOrder(OrderRequest orderRequest){
        // this block of code ensures that there won't be any address duplicates in db - if it's present in db, it'll be set as provided address
        // in order request
        Address address = addressService.getDuplicateAddressOrProvided(orderRequest.getAddress());
        orderRequest.setAddress(address);
        Order order = orderMapper.toEntity(orderRequest);

        // creating a list of OrderMealQuantity for later counting total price and saving it to db via cascade
        // by setting order it as a field in order entity
        List<MealQuantityRequest> mealQuantityRequestList = orderRequest.getMealQuantityRequestList();

        List<OrderMealQuantity> orderMealQuantityList = mealQuantityListMapper.fromRequestToEntity(mealQuantityRequestList, order);

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

        // return OrderResponse instead of Order entity
        OrderResponse orderResponse = orderMapper.toResponse(savedOrder);
        logger.info("Order created successfully");
        return orderResponse;
    }

    public List<OrderResponse> getAllOrdersByRestaurantId(Long restaurantId){
        // the condition below works this way: ROLE_ADMIN gets through anyway, ROLE_RESTAURANT_OWNER gets through only if they're the owner of the restaurant
        // variables are for clarity
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantService.getRestaurantByIdOrThrowExc(restaurantId);

        if(!user.getRole().equals(UserRole.ROLE_ADMIN) &&
                !restaurantService.isPrincipalOwnerOfRestaurant(restaurant)){
            throw new PrincipalIsNotAnOwnerException();
        }
        List<Order> orderList = orderRepository.findAllByRestaurantId(restaurantId);
        return getOrderResponses(orderList);
    }

    public List<OrderResponse> getAllOrdersByUserId(Long userId){
        List<Order> orderList = orderRepository.findAllByUserId(userId);

        // user can only access their orders, admins get through
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getRole().equals(UserRole.ROLE_CUSTOMER) && !Objects.equals(user.getId(), userId)){
            throw new PrincipalIsNotAnOwnerException();
        }

        return getOrderResponses(orderList);
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = getOrderByIdOrThrowExc(orderId);
        Restaurant restaurant = order.getRestaurant();

        // only restaurant owner can change their order status
        if (!restaurantService.isPrincipalOwnerOfRestaurant(restaurant)) {
            throw new PrincipalIsNotAnOwnerException();
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getOrderStatus().toUpperCase());
        } catch (Exception e) {
            throw new EnumConstantNotPresentException(request.getOrderStatus());
        }

        order.setOrderStatus(newStatus);
        return orderMapper.toResponse(order);
    }

    //method used internally to create a list of OrderResponses from a list of Orders from repo
    public List<OrderResponse> getOrderResponses(List<Order> orderList){
        return orderList.stream()
                .map(orderMapper::toResponse)
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
