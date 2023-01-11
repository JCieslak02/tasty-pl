package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class MealRequest {
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private BigDecimal price;
}
