package com.finalproject.Tiket.Pesawat.schedular;

import com.finalproject.Tiket.Pesawat.model.OtpForgotPassword;
import com.finalproject.Tiket.Pesawat.model.OtpRegister;
import com.finalproject.Tiket.Pesawat.repository.OtpForgotPasswordRepository;
import com.finalproject.Tiket.Pesawat.repository.OtpRegisterRepository;
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
    private OtpForgotPasswordRepository otpForgotPasswordRepository;

    @Autowired
    private OtpRegisterRepository otpRegisterRepository;

    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void deleteExpiredOTPForgotPassword() {
        List<OtpForgotPassword> expiredOTPs = otpForgotPasswordRepository.findAllByExpirationDateBefore(new Date());
        if (!expiredOTPs.isEmpty()){
            otpForgotPasswordRepository.deleteAll(expiredOTPs);
            log.info("sukses delete otp Forgot Password schedular");
        }
    }

    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void deleteExpiredOTPRegister() {
        List<OtpRegister> expiredOTPs = otpRegisterRepository.findAllByExpirationDateBefore(new Date());
        if (!expiredOTPs.isEmpty()){
            otpRegisterRepository.deleteAll(expiredOTPs);
            log.info("sukses delete otp Register schedular");
        }
    }

}
