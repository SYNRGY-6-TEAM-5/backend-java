package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseAirportDepartureArrivals;
import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseListAirport;

public interface AirportService  {

    ResponseListAirport getListOfAirport();

    ResponseAirportDepartureArrivals getDepartureAndArrivalBaseAiportId(Integer id);
}
