package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseAirportDepartureArrivals;
import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseListAirport;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.InternalServerHandling;
import com.finalproject.Tiket.Pesawat.model.Airport;
import com.finalproject.Tiket.Pesawat.repository.AirportRepository;
import com.finalproject.Tiket.Pesawat.specification.AirportSpecification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class AirportServiceImpl implements AirportService {

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public ResponseListAirport getListOfAirport(String search) {
        try {
            List<Airport> airportList = airportRepository.findAll(new AirportSpecification(search));
            if (airportList.isEmpty()) {
                throw new ExceptionHandling("Airport Empty");
            }

            return ResponseListAirport.builder()
                    .status(true)
                    .data(airportList)
                    .build();
        } catch (Exception e) {
            log.error("Error while processing the request", e);
            throw new InternalServerHandling("Internal Server Error occurred" + e.getMessage());
        }
    }

    @Override
    public ResponseAirportDepartureArrivals getDepartureAndArrivalBaseAiportId(Integer id) {
        Airport airport = airportRepository.findById(id).orElseThrow(()
                -> new ExceptionHandling("Airport not found with id " + id));
        return ResponseAirportDepartureArrivals.builder()
                .status(true)
                .airport(airport)
                .departures(airport.getDepartures())
                .arrivals(airport.getArrivals())
                .build();
    }

}
