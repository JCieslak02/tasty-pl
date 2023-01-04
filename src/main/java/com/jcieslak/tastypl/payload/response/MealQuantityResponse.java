package com.jcieslak.tastypl.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealQuantityResponse {
    private Long mealId;
    private String mealName;
    private String mealType;
    private BigDecimal mealPrice;
    private int quantity;
}
