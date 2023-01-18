package com.jcieslak.tastypl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int stars;
    private String comment;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return stars == review.stars && comment.equals(review.comment) && dateTime.equals(review.dateTime) && user.equals(review.user) && restaurant.equals(review.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stars, comment, dateTime, user, restaurant);
    }
}
