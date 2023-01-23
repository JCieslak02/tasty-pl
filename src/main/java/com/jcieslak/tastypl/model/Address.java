package com.jcieslak.tastypl.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String country;
    private String region;

    @Column(name="zip_code")
    private String zipCode;
    @Column
    private String city;
    private String street;
    @Column(name="building_number")
    private String buildingNumber;
    @Column(name="secondary_number")
    private String secondaryNumber;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address address)) return false;
        return (country.equals(address.country)
                && Objects.equals(region, address.region)
                && zipCode.equals(address.zipCode)
                && city.equals(address.city)
                && Objects.equals(street, address.street)
                && buildingNumber.equals(address.buildingNumber)
                && Objects.equals(secondaryNumber, address.secondaryNumber));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, region, zipCode, city, street, buildingNumber, secondaryNumber);
    }
}
