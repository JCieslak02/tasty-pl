package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.response.OrderResponse;
import com.jcieslak.tastypl.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final OrderService orderService;

    @GetMapping("{id}/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByUserId(@PathVariable("id") Long id){
        List<OrderResponse> orderResponseList = orderService.getAllOrdersByUserId(id);
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }
}
