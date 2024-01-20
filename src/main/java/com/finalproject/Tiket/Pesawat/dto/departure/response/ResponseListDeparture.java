package com.finalproject.Tiket.Pesawat.dto.departure.response;

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
public class ResponseListDeparture {
    private Boolean status;
    private List<Departure> data;
}
