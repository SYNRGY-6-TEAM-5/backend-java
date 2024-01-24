package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseAirportDepartureArrivals;
import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseListAirport;
import com.finalproject.Tiket.Pesawat.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airport")
public class AirportController {

    @Autowired
    private AirportService airportService;


    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseListAirport> listAirportResponse(
            @RequestParam(value = "search", required = false) String search) {
        ResponseListAirport response = airportService.getListOfAirport(search);
        return ResponseEntity.ok(response);
    }

    // get departure and arrivals based on airport id
    @GetMapping(value = "/{id}/departures-arrivals", produces = "application/json")
    public ResponseEntity<ResponseAirportDepartureArrivals> departureArrivalsResponseEntity(
            @PathVariable(value = "id") Integer id) {
        ResponseAirportDepartureArrivals response = airportService.getDepartureAndArrivalBaseAiportId(id);
        return ResponseEntity.ok(response);
    }

    // search Airport based on name
}
