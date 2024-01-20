package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.arrival.response.ResponseListArrival;
import com.finalproject.Tiket.Pesawat.service.ArrivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/arrival")
public class ArrivalController {


    @Autowired
    private ArrivalService arrivalService;


    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseListArrival> arrivalResponseEntity() {
        ResponseListArrival response = arrivalService.getListOfArrival();
        return ResponseEntity.ok(response);
    }
}
