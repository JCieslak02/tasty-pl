package com.jcieslak.tastypl.meal;

import com.jcieslak.tastypl.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="meal_name")
    private String name;
    private BigDecimal price;
    private String type;

    @ManyToOne
    @JoinColumn(name="restaurant_id", insertable=false, updatable=false)
    private Restaurant restaurant;

    @Column(name="restaurant_id")
    private Long restaurantId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return name.equals(meal.name) && (price.compareTo(meal.price) == 0) && type.equals(meal.type) && restaurantId.equals(meal.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, type);
    }
}
