package com.finalproject.Tiket.Pesawat.dto.arrival.response;

import com.finalproject.Tiket.Pesawat.model.Arrival;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseListArrival {
    private Boolean status;
    private List<Arrival> arrivals;
}
