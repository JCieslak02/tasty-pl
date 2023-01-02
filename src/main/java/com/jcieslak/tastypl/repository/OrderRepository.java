package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByRestaurantId(Long id);
}
