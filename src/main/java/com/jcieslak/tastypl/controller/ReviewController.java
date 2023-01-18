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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewResponse> updateReview(@Valid @RequestBody ReviewRequest reviewRequest, @PathVariable Long id){
        ReviewResponse response = reviewService.updateReview(id, reviewRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
