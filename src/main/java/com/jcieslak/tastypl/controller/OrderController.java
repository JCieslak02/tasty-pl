package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.request.OrderRequest;
import com.jcieslak.tastypl.payload.request.OrderStatusUpdateRequest;
import com.jcieslak.tastypl.payload.response.OrderResponse;
import com.jcieslak.tastypl.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByRestaurantId(@PathVariable Long restaurantId){
        List<OrderResponse> orderResponseList = orderService.getAllOrdersByRestaurantId(restaurantId);
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ROLE_RESTAURANT_OWNER')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateRequest request){
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, request);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
