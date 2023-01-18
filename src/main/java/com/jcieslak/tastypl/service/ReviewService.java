package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.mapper.ReviewMapper;
import com.jcieslak.tastypl.model.Order;
import com.jcieslak.tastypl.model.Review;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.ReviewRequest;
import com.jcieslak.tastypl.payload.response.ReviewResponse;
import com.jcieslak.tastypl.repository.OrderRepository;
import com.jcieslak.tastypl.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    private final RestaurantService restaurantService;

    public Review getReviewById(long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review", reviewId));
    }

    public List<ReviewResponse> getAllReviewsByRestaurantId(long restaurantId){
        List<Review> reviewList = reviewRepository.findAllByRestaurantId(restaurantId);

        return reviewMapper.toResponseList(reviewList);
    }
    public ReviewResponse createReview(ReviewRequest reviewRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        Long restaurantId = reviewRequest.getRestaurantId();

        // has user ever ordered from this restaurant
        Order order = orderRepository.findFirstByUserIdAndRestaurantId(userId, restaurantId);
        if(order == null){
            throw new IllegalArgumentException("User hasn't placed any orders in this restaurant");
        }

        // has user already written a review in this restaurant
        Review placedReview = reviewRepository.findFirstByUserIdAndRestaurantId(userId, restaurantId);
        if(placedReview != null){
            throw new IllegalArgumentException("User has already reviewed this restaurant");
        }

        Review review = reviewMapper.toEntity(reviewRequest);
        review.setUser(user);
        review.setDateTime(LocalDateTime.now());

        // updating review fields in restaurant
        restaurantService.updateReviewScoreAndReviewCount(restaurantId, reviewRequest.getStars(), 1);

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toResponse(savedReview);
    }

    public void deleteReview(Long reviewId){
        Review review = getReviewById(reviewId);
        int starsNegative = review.getStars() * (-1);

        // updating review fields in restaurant
        restaurantService.updateReviewScoreAndReviewCount(review.getRestaurant().getId(), starsNegative, 2);

        reviewRepository.delete(review);
    }

    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest){
        Review dbReview = getReviewById(reviewId);

        int oldStars = dbReview.getStars();
        int starsToPass = reviewRequest.getStars() - oldStars;

        // updating review fields in restaurant
        restaurantService.updateReviewScoreAndReviewCount(dbReview.getRestaurant().getId(), starsToPass, 0);

        dbReview.setComment(reviewRequest.getComment());
        dbReview.setDateTime(LocalDateTime.now());
        dbReview.setStars(reviewRequest.getStars());

        reviewRepository.save(dbReview);

        return reviewMapper.toResponse(dbReview);
    }
}
