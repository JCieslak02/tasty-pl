package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findFirstByUserIdAndRestaurantId(Long userId, Long restaurantId);
    List<Review> findAllByRestaurantId(Long restaurantId);

    List<Review> findAllByUserId(long userId);
}
