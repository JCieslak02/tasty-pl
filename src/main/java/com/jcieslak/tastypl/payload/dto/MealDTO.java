package com.jcieslak.tastypl.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class MealDTO {
    private String name;
    private String type;
    private BigDecimal price;
    private Long restaurantId;
}
