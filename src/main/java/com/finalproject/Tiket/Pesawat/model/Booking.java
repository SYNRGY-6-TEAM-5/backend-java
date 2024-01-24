package com.finalproject.Tiket.Pesawat.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "trip_type")
    private String tripType;

    @Column(name = "total_passenger")
    private Integer totalPassenger;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_time")
    private Date expiredTime;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "full_protection")
    private Boolean fullProtection;

    @Column(name = "bag_insurance")
    private Boolean bagInsurance;

    @Column(name = "flight_delay")
    private Boolean flightDelay;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


}
