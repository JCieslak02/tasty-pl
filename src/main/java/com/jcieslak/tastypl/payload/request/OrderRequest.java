package com.jcieslak.tastypl.payload.request;

import com.jcieslak.tastypl.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {
    @NonNull
    private Long restaurantId;
    @NonNull
    private Address address;
    @NonNull
    private List<MealQuantityRequest> mealQuantityRequestList;
}
