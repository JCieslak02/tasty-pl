package com.jcieslak.tastypl.restaurant;

import com.jcieslak.tastypl.contact.Contact;
import com.jcieslak.tastypl.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "restaurant_name")
    private String name;

    @Column(name = "restaurant_type")
    private String type;
    @OneToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;

    //TODO: orders
    @OneToMany(mappedBy = "restaurant")
    @ToString.Exclude
    private Set<Order> orders;

    public Restaurant(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
