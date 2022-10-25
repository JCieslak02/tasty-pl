package com.jcieslak.tastypl.address;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String country;
    private String region;
    @Column(name="zip_code")
    private String zipCode;
    private String city;
    @Column(name="building_number")
    private String buildingNumber;
    @Column(name="secondary_number")
    private String secondaryNumber;

    public Address(String country, String region, String zipCode, String city, String buildingNumber, String secondaryNumber) {
        this.country = country;
        this.region = region;
        this.zipCode = zipCode;
        this.city = city;
        this.buildingNumber = buildingNumber;
        this.secondaryNumber = secondaryNumber;
    }
}
