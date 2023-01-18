package com.jcieslak.tastypl.mapper;

import com.jcieslak.tastypl.model.Review;
import com.jcieslak.tastypl.payload.request.ReviewRequest;
import com.jcieslak.tastypl.payload.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final ModelMapper modelMapper;

    public Review toEntity(ReviewRequest reviewRequest){
        return modelMapper.map(reviewRequest, Review.class);
    }

    public ReviewResponse toResponse(Review review){
        return modelMapper.map(review, ReviewResponse.class);
    }

    public List<ReviewResponse> toResponseList(List<Review> reviewList){
        return reviewList.stream()
                .map(r -> modelMapper.map(r, ReviewResponse.class))
                .toList();
    }
}
