package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UserRequestBooking;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserBookingResponse;
import com.finalproject.Tiket.Pesawat.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getBookingByUser();

    UserBookingResponse createBookingByUser(UserRequestBooking userRequestBooking);
}
