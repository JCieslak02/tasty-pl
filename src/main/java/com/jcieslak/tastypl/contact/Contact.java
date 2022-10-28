package com.jcieslak.tastypl.contact;

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
@Table(name="contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String tel;

    public Contact(String email, String tel) {
        this.email = email;
        this.tel = tel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return email.equals(contact.email) && tel.equals(contact.tel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, tel);
    }
}
