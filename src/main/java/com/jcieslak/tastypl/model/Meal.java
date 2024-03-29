package com.jcieslak.tastypl.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="meal_name")
    private String name;
    private BigDecimal price;
    private String type;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return name.equals(meal.name) && (price.compareTo(meal.price) == 0) && type.equals(meal.type) && restaurant.equals(meal.getRestaurant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, type);
    }
}
