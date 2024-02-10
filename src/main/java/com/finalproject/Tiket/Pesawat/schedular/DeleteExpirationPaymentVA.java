package com.finalproject.Tiket.Pesawat.schedular;

import com.finalproject.Tiket.Pesawat.model.Booking;
import com.finalproject.Tiket.Pesawat.repository.BookingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_FAILED;
import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_PAYMENT_STATUS_PENDING;

@Component
@Log4j2
public class DeleteExpirationPaymentVA {

    @Autowired
    private BookingRepository bookingRepository;

//    @Scheduled(fixedRate = 60000) // berjalan setiap 60 detik
//    public void updatePaymentStatusVAExpired() {
//        List<Booking> pendingExpiredBookings = bookingRepository.findAllByExpiredTimeBeforeAndStatus(new Date(),
//                CONSTANT_PAYMENT_STATUS_PENDING);
//        if (!pendingExpiredBookings.isEmpty()) {
//            updateBookingStatus(pendingExpiredBookings);
//        }
//    }

    private void updateBookingStatus(List<Booking> bookings) {
        List<Booking> updatedBookings = bookings.stream()
                .peek(booking -> booking.setStatus(CONSTANT_PAYMENT_STATUS_FAILED))
                .collect(Collectors.toList());

        bookingRepository.saveAll(updatedBookings);
        log.info("Berhasil memperbarui status menjadi GAGAL untuk scheduler VA yang kadaluarsa");
    }

}
