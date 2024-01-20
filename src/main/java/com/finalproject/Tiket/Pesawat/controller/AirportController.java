package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseAirportDepartureArrivals;
import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseListAirport;
import com.finalproject.Tiket.Pesawat.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airport")
public class AirportController {

    @Autowired
    private AirportService airportService;


    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseListAirport> listAirportResponse(){
        ResponseListAirport response = airportService.getListOfAirport();
        return ResponseEntity.ok(response);
    }

    // get departure and arrivals based on airport id
    @GetMapping(value = "/{id}/departures-arrivals",produces = "application/json")
    public ResponseEntity<ResponseAirportDepartureArrivals> departureArrivalsResponseEntity(
            @PathVariable(value = "id") Integer id){
        ResponseAirportDepartureArrivals response = airportService.getDepartureAndArrivalBaseAiportId(id);
        return ResponseEntity.ok(response);
    }

    // search Airport based on name
}
