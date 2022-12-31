package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
