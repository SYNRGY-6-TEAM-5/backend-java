package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer>, JpaSpecificationExecutor<Airport> {



}
