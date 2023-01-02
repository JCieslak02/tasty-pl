package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findContactByEmail(String email);
    Optional<Contact> findContactByPhoneNumber(String tel);
}
