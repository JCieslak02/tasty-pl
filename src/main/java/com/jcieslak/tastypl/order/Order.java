package com.jcieslak.tastypl.order;

import com.jcieslak.tastypl.address.Address;
import com.jcieslak.tastypl.client.Client;
import com.jcieslak.tastypl.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Column(name="is_finished")
    private boolean isFinished;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    @ToString.Exclude
    private Client client;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    @ToString.Exclude
    private Restaurant restaurant;

    public Order(LocalDateTime dateTime, BigDecimal price) {
        this.dateTime = dateTime;
        this.isFinished = false;
        this.price = price;
    }
}
