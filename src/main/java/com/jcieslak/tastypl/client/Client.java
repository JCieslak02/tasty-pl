package com.jcieslak.tastypl.client;

import com.jcieslak.tastypl.address.Address;
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
@Table(name="client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @JoinColumn(name = "address_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Address address;

    @OneToMany(mappedBy = "client")
    private Set<Order> orders;
    @OneToOne
    @JoinColumn(name="contact_id", referencedColumnName = "id")
    private Contact contact;

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
