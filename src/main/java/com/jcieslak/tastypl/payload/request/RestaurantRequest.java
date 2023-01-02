package com.jcieslak.tastypl.payload.request;

import com.jcieslak.tastypl.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RestaurantRequest {
    @NonNull
    private String restaurantName;
    @NonNull
    private String restaurantType;
    @NonNull
    private Address address;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String email;
}
