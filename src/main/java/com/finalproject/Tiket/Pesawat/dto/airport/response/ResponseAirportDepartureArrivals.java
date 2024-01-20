package com.finalproject.Tiket.Pesawat.dto.airport.response;

import com.finalproject.Tiket.Pesawat.model.Airport;
import com.finalproject.Tiket.Pesawat.model.Arrival;
import com.finalproject.Tiket.Pesawat.model.Departure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAirportDepartureArrivals {
    private Boolean status;
    private Airport airport;
    private List<Departure> departures;
    private List<Arrival> arrivals;
}
