package com.example.securingweb.repo;

import com.example.securingweb.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
}
