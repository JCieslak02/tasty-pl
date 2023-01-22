package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.request.ReviewRequest;
import com.jcieslak.tastypl.payload.response.ReviewResponse;
import com.jcieslak.tastypl.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest){
        ReviewResponse response = reviewService.createReview(reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewResponse> updateReview(@Valid @RequestBody ReviewRequest reviewRequest, @PathVariable Long reviewId){
        ReviewResponse response = reviewService.updateReview(reviewId, reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsByRestaurantId(@PathVariable Long restaurantId){
        List<ReviewResponse> reviewResponseList = reviewService.getAllReviewsByRestaurantId(restaurantId);
        return new ResponseEntity<>(reviewResponseList, HttpStatus.OK);
    }
}
