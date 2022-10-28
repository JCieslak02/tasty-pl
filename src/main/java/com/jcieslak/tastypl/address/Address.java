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
    private String country;
    private String region;
    @Column(name="zip_code")
    private String zipCode;
    private String city;
    private String street;
    @Column(name="building_number")
    private String buildingNumber;
    @Column(name="secondary_number")
    private String secondaryNumber;

    public Address(String country, String region, String zipCode, String city, String street, String buildingNumber, String secondaryNumber) {
        this.country = country;
        this.region = region;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.secondaryNumber = secondaryNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return country.equals(address.country) && region.equals(address.region) && zipCode.equals(address.zipCode) && city.equals(address.city) && street.equals(address.street) && buildingNumber.equals(address.buildingNumber) && Objects.equals(secondaryNumber, address.secondaryNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, region, zipCode, city, street, buildingNumber, secondaryNumber);
    }
}
