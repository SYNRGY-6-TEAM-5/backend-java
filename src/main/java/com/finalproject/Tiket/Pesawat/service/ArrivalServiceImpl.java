package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.arrival.response.ResponseListArrival;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.model.Arrival;
import com.finalproject.Tiket.Pesawat.repository.ArrivalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArrivalServiceImpl implements ArrivalService {

    private ArrivalRepository arrivalRepository;

    @Override
    public ResponseListArrival getListOfArrival() {
        List<Arrival> arrivalList = arrivalRepository.findAllWithAirport();
        if (arrivalList.isEmpty()){
            throw new ExceptionHandling("Arrival Empty");
        }
        return ResponseListArrival.builder()
                .status(true)
                .arrivals(arrivalList)
                .build();
    }
}
