package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.SuccesMessageDTO;
import com.finalproject.Tiket.Pesawat.dto.booking.request.DeleteBookingRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.DeleteUserRequest;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.service.BookingService;
import com.finalproject.Tiket.Pesawat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/all-user")
    public ResponseEntity<List<User>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<User> response = userService.getAllUser(page, size);
            return ResponseEntity.ok(response);
        } catch (ExceptionHandling exception) {
            throw new ExceptionHandling(exception.getMessage());
        }
    }

    @GetMapping("/all-booking")
    public ResponseEntity<List<Booking>> getAllBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Booking> response = bookingService.getAllBooking(page, size);
            return ResponseEntity.ok(response);
        } catch (ExceptionHandling exception) {
            throw new ExceptionHandling(exception.getMessage());
        }
    }

    @DeleteMapping("/booking")
    public ResponseEntity<SuccesMessageDTO> deleteBooking(@RequestBody DeleteBookingRequest deleteBookingRequest) {
        SuccesMessageDTO response = bookingService.deleteBookingById(deleteBookingRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user")
    public ResponseEntity<SuccesMessageDTO> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        SuccesMessageDTO response = userService.deleteUserById(deleteUserRequest);
        return ResponseEntity.ok(response);
    }
}