package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.OtpRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRegisterRepository extends JpaRepository<OtpRegister, UUID> {
    Optional<OtpRegister> findByEmailUser(String email);

    List<OtpRegister> findAllByExpirationDateBefore(Date now);

}
