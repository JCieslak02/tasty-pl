package com.jcieslak.tastypl.meal;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/meals")
public class MealController {
    private final MealService mealService;
    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals(){
        List<Meal> meals = mealService.getAllMeals();
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal){
        Meal createdMeal = mealService.createMeal(meal);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }
}
