package com.jcieslak.tastypl.payload.response;

import com.jcieslak.tastypl.enums.OrderStatus;
import com.jcieslak.tastypl.model.Address;
import com.jcieslak.tastypl.payload.request.MealRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {
    private Long id;
    private LocalDateTime dateTime;
    private BigDecimal total;
    private OrderStatus status;
    private Address address;
    private List<MealRequest> meals;
}
