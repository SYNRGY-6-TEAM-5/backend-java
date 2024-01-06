package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
