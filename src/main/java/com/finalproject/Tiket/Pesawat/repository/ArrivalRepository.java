package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.Arrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Integer> {

    @Query("SELECT a FROM Arrival a JOIN FETCH a.airport")
    List<Arrival> findAllWithAirport();


}
