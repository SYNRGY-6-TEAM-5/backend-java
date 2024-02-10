package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer>, JpaSpecificationExecutor<Booking> {
    List<Booking> findAllByUser(User user);

    Optional<Booking> findByExternalId(String externalId);

    List<Booking> findAllByExpiredTimeBefore(Date date);
}

