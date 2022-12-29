package com.jcieslak.tastypl.repository;

import com.jcieslak.tastypl.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
