package com.jcieslak.tastypl.address;

import lombok.Data;

@Data
public class AddressDto{
    private Long id;
    private String country;
    private String region;
    private String zipCode;
    private String city;
    private String street;
    private String buildingNumber;
    private String secondaryNumber;
}