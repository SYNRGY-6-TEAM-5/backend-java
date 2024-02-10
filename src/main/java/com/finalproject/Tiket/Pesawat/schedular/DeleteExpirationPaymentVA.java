package com.finalproject.Tiket.Pesawat.schedular;

import com.finalproject.Tiket.Pesawat.dto.firebase.request.NotificationRequest;
import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import com.finalproject.Tiket.Pesawat.service.FCMService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_FAILED;
import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_PENDING;

@Component
@Log4j2
public class DeleteExpirationPaymentVA {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FCMService fcmService;

    @Scheduled(fixedRate = 60000)
    public void updatePaymentStatusVAExpired() {
        List<Booking> pendingExpiredBookings = bookingRepository.findAllByExpiredTimeBeforeAndStatus(new Date(),
                CONSTANT_PAYMENT_STATUS_PENDING);
        if (!pendingExpiredBookings.isEmpty()) {
            updateBookingStatus(pendingExpiredBookings);
        }
    }

    private void updateBookingStatus(List<Booking> bookings) {
        List<Booking> updatedBookings = bookings.stream()
                .peek(booking -> booking.setStatus(CONSTANT_PAYMENT_STATUS_FAILED))
                .collect(Collectors.toList());
        log.info("Berhasil memperbarui status menjadi GAGAL untuk scheduler VA yang kadaluarsa");
        bookingRepository.saveAll(updatedBookings);
        // Send notification for each booking with failed payment
        sendNotificationForFailedPayments(updatedBookings);
    }

    private void sendNotificationForFailedPayments(List<Booking> failedBookings) {
        failedBookings.forEach(booking -> {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("Payment Failed")
                    .body("Your payment for booking with ID " + booking.getBookingId() + " has failed.")
                    .token(booking.getUser().getFcmToken())
                    .build();
            try {
                fcmService.sendMessageToToken(notificationRequest);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error sending notification for payment failure: " + e.getMessage());
            }
            log.info("Success Send Failed Message FCM");
        });
    }

}
