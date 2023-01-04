package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class MealRequest {
    @NonNull
    private String name;
    @NonNull
    private String type;
    @NonNull
    private BigDecimal price;
}
