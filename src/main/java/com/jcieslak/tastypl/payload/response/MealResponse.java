package com.jcieslak.tastypl.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealResponse {
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;
}
