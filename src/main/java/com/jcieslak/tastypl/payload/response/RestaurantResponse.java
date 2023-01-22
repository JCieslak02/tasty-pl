package com.jcieslak.tastypl.payload.response;

import com.jcieslak.tastypl.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RestaurantResponse {
    private long id;
    private String restaurantName;
    private String restaurantType;
    private Address address;
    private String phoneNumber;
    private String email;
    private long ownerId;
    private double rating;
    private int reviewCount;
}
