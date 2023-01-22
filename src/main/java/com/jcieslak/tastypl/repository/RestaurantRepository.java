package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByAddressCityAndType(String city, String type, Pageable pageable);

    Page<Restaurant> findByAddressCity(String city, Pageable pageable);
}
