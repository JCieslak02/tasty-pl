package com.jcieslak.tastypl.payload.request;

import com.jcieslak.tastypl.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {
    @NotNull
    private Long restaurantId;
    @NotNull
    private Address address;
    @NotNull
    private List<MealQuantityRequest> mealQuantityRequestList;
}
