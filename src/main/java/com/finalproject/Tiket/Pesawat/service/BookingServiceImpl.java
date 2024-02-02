package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getBookingByUser() {
        return bookingRepository.findAll();
    }
}
