package com.finalproject.Tiket.Pesawat.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "otp_register")
public class OtpRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "email", unique = true)
    private String emailUser;

    @Column(name = "password")
    private String password;

    @Column(name = "otp_code")
    private String otp;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Date generateDate;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exp_time")
    private Date expirationDate;
}
