package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.OtpForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpForgotPasswordRepository extends JpaRepository<OtpForgotPassword, UUID> {
    Optional<OtpForgotPassword> findByEmailUser(String email);
    List<OtpForgotPassword> findAllByExpirationDateBefore(Date now);
}
