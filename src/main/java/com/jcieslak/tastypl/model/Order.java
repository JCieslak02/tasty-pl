package com.jcieslak.tastypl.model;

import com.jcieslak.tastypl.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="orders")
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderMealQuantity> orderMealQuantityList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return dateTime.equals(order.dateTime) && orderStatus.equals(order.orderStatus) && total.equals(order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, orderStatus, total);
    }
}
