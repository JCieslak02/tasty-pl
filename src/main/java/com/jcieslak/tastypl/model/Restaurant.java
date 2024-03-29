package com.jcieslak.tastypl.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="restaurant")
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_name")
    private String name;

    @Column(name = "restaurant_type")
    private String type;

    private String email;

    private double rating;

    @Column(name="review_count")
    private int reviewCount;
    @Column(name = "phone_number")
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return name.equals(that.name) && type.equals(that.type) && email.equals(that.email) && phoneNumber.equals(that.phoneNumber) && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, email, phoneNumber, address);
    }
}
