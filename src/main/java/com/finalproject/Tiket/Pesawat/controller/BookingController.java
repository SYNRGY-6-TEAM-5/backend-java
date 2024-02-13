package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.user.request.UserRequestBooking;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserBookingResponse;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<UserBookingResponse> userCreateBooking(@Valid @RequestBody UserRequestBooking userRequestBooking) {
        UserBookingResponse response = bookingService.createBookingByUser(userRequestBooking);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookingByUserId() {
        List<Booking> response = bookingService.getBookingByUser();
        return ResponseEntity.ok(response);
    }



}
