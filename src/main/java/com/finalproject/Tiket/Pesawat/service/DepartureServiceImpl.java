package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.departure.response.ResponseListDeparture;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.InternalServerHandling;
import com.finalproject.Tiket.Pesawat.model.Departure;
import com.finalproject.Tiket.Pesawat.repository.DepartureRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class DepartureServiceImpl implements DepartureService {

    @Autowired
    private DepartureRepository departureRepository;

    @Override
    public ResponseListDeparture getListDeparture() {

        try {
            List<Departure> departureList = departureRepository.findAllWithAirport();

            if (departureList.isEmpty()){
                throw new ExceptionHandling("Departure Empty");
            }

            return ResponseListDeparture.builder()
                    .status(true)
                    .data(departureList)
                    .build();

        } catch (Exception e){
            log.error("errror in departure service " + e.getMessage() + " " + e.getMessage());
            throw new InternalServerHandling(e.getMessage());
        }

    }
}
