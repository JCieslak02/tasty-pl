package com.jcieslak.tastypl.contact;

import com.jcieslak.tastypl.client.Client;
import com.jcieslak.tastypl.restaurant.Restaurant;
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
@Table(name="contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String tel;

    @OneToOne(mappedBy = "contact")
    private Client client;

    @OneToOne(mappedBy = "contact")
    private Restaurant restaurant;

    public Contact(String email, String tel) {
        this.email = email;
        this.tel = tel;
    }
}
