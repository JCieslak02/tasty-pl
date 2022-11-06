package com.jcieslak.tastypl.address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String country;
    private String region;

    @Column(name="zip_code", nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String city;
    private String street;
    @Column(name="building_number", nullable = false)
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
