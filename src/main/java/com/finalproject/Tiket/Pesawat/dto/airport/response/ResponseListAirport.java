package com.finalproject.Tiket.Pesawat.dto.airport.response;

import com.finalproject.Tiket.Pesawat.model.Airport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseListAirport {
    private Boolean status;
    private List<Airport> data;
}
