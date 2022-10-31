package com.jcieslak.tastypl.contact;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findContactByEmail(String email);
    Optional<Contact> findContactByTel(String tel);
}
