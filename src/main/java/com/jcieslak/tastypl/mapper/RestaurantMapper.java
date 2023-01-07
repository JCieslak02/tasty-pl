package com.jcieslak.tastypl.mapper;

import com.jcieslak.tastypl.model.Restaurant;
import com.jcieslak.tastypl.payload.request.RestaurantRequest;
import com.jcieslak.tastypl.payload.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    private final ModelMapper modelMapper;

    public Restaurant toEntity(RestaurantRequest restaurantRequest){
        return modelMapper.map(restaurantRequest, Restaurant.class);
    }

    public RestaurantResponse toResponse(Restaurant restaurant){
        return modelMapper.map(restaurant, RestaurantResponse.class);
    }
}
