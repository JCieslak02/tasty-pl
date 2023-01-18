package com.jcieslak.tastypl.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private int stars;
    private Long userId;
    private Long restaurantId;
    private String comment;
    private LocalDateTime dateTime;
}
