package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findAllByRestaurantId(Long id);
}