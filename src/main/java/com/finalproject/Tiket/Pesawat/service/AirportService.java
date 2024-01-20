package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseListAirport;
import com.finalproject.Tiket.Pesawat.dto.airport.response.ResponseAirportDepartureArrivals;

public interface AirportService  {

    ResponseListAirport getListOfAirport();

    ResponseAirportDepartureArrivals getDepartureAndArrivalBaseAiportId(Integer id);
}
