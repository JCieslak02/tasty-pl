package com.jcieslak.tastypl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MealIsFromDifferentRestaurantException extends RuntimeException{
    public MealIsFromDifferentRestaurantException() {
        super("Provided meal is from another restaurant");
    }
}
