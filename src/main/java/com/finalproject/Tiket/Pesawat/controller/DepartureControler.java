package com.finalproject.Tiket.Pesawat.controller;


import com.finalproject.Tiket.Pesawat.dto.departure.response.ResponseListDeparture;
import com.finalproject.Tiket.Pesawat.service.DepartureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departure")
public class DepartureControler {

    @Autowired
    private DepartureService departureService;


    @GetMapping()
    public ResponseEntity<ResponseListDeparture> departureResponseEntity() {
        ResponseListDeparture response = departureService.getListDeparture();
        return ResponseEntity.ok(response);
    }


}
