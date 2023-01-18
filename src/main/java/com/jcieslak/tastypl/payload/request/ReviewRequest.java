package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class ReviewRequest {
    // stars must be between 1-5
    @Min(1)
    @Max(5)
    private int stars;
    @NotNull
    private Long restaurantId;
    private String comment;
}
