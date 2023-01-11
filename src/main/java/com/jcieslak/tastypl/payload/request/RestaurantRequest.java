package com.jcieslak.tastypl.payload.request;

import com.jcieslak.tastypl.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class RestaurantRequest {
    @NotNull
    private String restaurantName;
    @NotNull
    private String restaurantType;
    @NotNull
    private Address address;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String email;
}
