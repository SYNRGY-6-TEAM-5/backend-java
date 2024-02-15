package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.SuccesMessageDTO;
import com.finalproject.Tiket.Pesawat.dto.booking.request.DeleteBookingRequest;
import com.finalproject.Tiket.Pesawat.dto.firebase.request.NotificationRequest;
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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_PENDING;
import static com.finalproject.Tiket.Pesawat.utils.Constants.PAYMENT_EXPIRED_TIME;

@Service
@Log4j2
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailService emailService;


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FCMService fcmService;

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
            } else if (principal instanceof OAuth2User) {
                log.info("oauth2 user bos" + ((OAuth2User) principal).getName() + ((OAuth2User) principal).getAttributes());
                Optional<User> userOptional = userRepository.findByEmailAddress(((OAuth2User) principal).getName());

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
                        .totalPassenger(userRequestBooking.getTotal_passenger())
                        .totalAmount(userRequestBooking.getTotal_amount())
                        .expiredTime(new Date(System.currentTimeMillis() + PAYMENT_EXPIRED_TIME))
                        .fullProtection(userRequestBooking.getFull_protection())
                        .bagInsurance(userRequestBooking.getBag_insurance())
                        .status(CONSTANT_PAYMENT_STATUS_PENDING)
                        .flightDelay(userRequestBooking.getFlight_delay())
                        .paymentMethod(userRequestBooking.getPayment_method())
                        .createdAt(Utils.getCurrentDateTimeAsDate())
                        .user(userOptional.get())
                        .createdAt(Utils.getCurrentDateTimeAsDate())
                        .build();

                bookingRepository.save(bookingUser);
                log.info("success create booking");

                NotificationRequest bookingCreationNotification = NotificationRequest.builder()
                        .title("Booking Created Successfully")
                        .body("Your booking has been successfully created.")
                        .token(bookingUser.getUser().getFcmToken())
                        .build();

                if(userOptional.get().getFcmToken() != null) {
                    try {
                        fcmService.sendMessageToToken(bookingCreationNotification);
                    } catch (InterruptedException | ExecutionException e) {
                        log.error(e.getMessage());
                    }
                }

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
        if (bookingPage.isEmpty()) {
            throw new ExceptionHandling("Booking is Empty");
        }
        return bookingPage.getContent();
    }

    @Override
    public SuccesMessageDTO deleteBookingById(DeleteBookingRequest deleteBookingRequest) {
        try {
            Optional<Booking> bookingOptional = bookingRepository.findById(deleteBookingRequest.getBookingId());
            if (bookingOptional.isEmpty()) {
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