package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAddress(String username);
}
