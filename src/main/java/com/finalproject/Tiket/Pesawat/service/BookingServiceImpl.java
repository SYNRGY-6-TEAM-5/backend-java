package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.SuccesMessageDTO;
import com.finalproject.Tiket.Pesawat.dto.booking.request.DeleteBookingRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UserRequestBooking;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserBookingResponse;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class BookingServiceImpl implements BookingService {


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Booking> getBookingByUser() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;

                Optional<User> userOptional = userRepository.findByEmailAddress(userDetails.getUsername());

                if (userOptional.isEmpty()) {
                    throw new UnauthorizedHandling("User Not Found");
                }

                User user = userOptional.get();
                List<Booking> booking = bookingRepository.findAllByUser(user);

                if (booking.isEmpty()) {
                    throw new ExceptionHandling("Empty Bookings");
                }

                return booking;
            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }
        throw new ExceptionHandling("Unexpected state: No return statement reached");
    }


    @Override
    public UserBookingResponse createBookingByUser(UserRequestBooking userRequestBooking) {
        Booking bookingUser = null;
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                Optional<User> userOptional = userRepository
                        .findByEmailAddress(((UserDetailsImpl) principal).getUsername());
                if (userOptional.isEmpty()) {
                    throw new UnauthorizedHandling("User Not Found");
                }
                bookingUser = Booking.builder()
                        .tripType(userRequestBooking.getTripe_type())
                        .totalPassenger(userRequestBooking.getTotal_passenger())
                        .expiredTime(userRequestBooking.getExpired_time())
                        .totalAmount(userRequestBooking.getTotal_amount())
                        .fullProtection(userRequestBooking.getFull_protection())
                        .bagInsurance(userRequestBooking.getBag_insurance())
                        .flightDelay(userRequestBooking.getFlight_delay())
                        .paymentMethod(userRequestBooking.getPayment_method())
                        .status(userRequestBooking.getStatus())
                        .createdAt(Utils.getCurrentDateTimeAsDate())
                        .user(userOptional.get())
                        .createdAt(Utils.getCurrentDateTimeAsDate())
                        .build();

                bookingRepository.save(bookingUser);
                log.info("success create booking");

            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }

        return UserBookingResponse.builder()
                .success(true)
                .bookingId(bookingUser.getBookingId().toString())
                .message("success create booking")
                .build();

    }
    @Override
    public List<Booking> getAllBooking(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);
        if (bookingPage.isEmpty()){
            throw new ExceptionHandling("Booking is Empty");
        }
        return bookingPage.getContent();
    }

    @Override
    public SuccesMessageDTO deleteBookingById(DeleteBookingRequest deleteBookingRequest) {
        try {
            Optional<Booking> bookingOptional = bookingRepository.findById(deleteBookingRequest.getBookingId());
            if (bookingOptional.isEmpty()){
                throw new ExceptionHandling("Booking Not Found");
            }

            bookingRepository.delete(bookingOptional.get());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExceptionHandling(e.getMessage());
        }
        return SuccesMessageDTO.builder()
                .success(true)
                .message("success delete booking")
                .build();
    }
}