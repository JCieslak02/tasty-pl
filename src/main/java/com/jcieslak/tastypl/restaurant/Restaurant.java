package com.jcieslak.tastypl.restaurant;

import com.jcieslak.tastypl.address.Address;
import com.jcieslak.tastypl.contact.Contact;
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
@Table(name="restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_name")
    private String name;

    @Column(name = "restaurant_type")
    private String type;

    @OneToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    @OneToOne
    @JoinColumn(name="address_id", referencedColumnName = "id")
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return name.equals(that.name) && type.equals(that.type) && contact.equals(that.contact) && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, contact, address);
    }
}
