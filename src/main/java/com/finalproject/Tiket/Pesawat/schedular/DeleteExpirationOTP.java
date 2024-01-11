package com.finalproject.Tiket.Pesawat.schedular;

import com.finalproject.Tiket.Pesawat.model.OtpInfo;
import com.finalproject.Tiket.Pesawat.repository.OtpRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class DeleteExpirationOTP {

    @Autowired
    private OtpRepository otpRepository;

    // todo dibuat constant otp service bisa mengikuti
    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void deleteExpiredOTPs() {
        List<OtpInfo> expiredOTPs = otpRepository.findAllByExpirationDateBefore(new Date());
        if (!expiredOTPs.isEmpty()){
            otpRepository.deleteAll(expiredOTPs);
            log.info("sukses delete otp schedular");
        }
    }

}
