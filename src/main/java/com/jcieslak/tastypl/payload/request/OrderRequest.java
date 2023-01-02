package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private Long restaurantId;
    private List<OrderMealQuantityRequest> orderMealQuantities;
}
