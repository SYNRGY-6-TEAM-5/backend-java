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
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "transit")
    private Integer transit;

    @Column(name = "first_seat")
    private Integer firstSeat;

    @Column(name = "business_seat")
    private Integer businessSeat;

    @Column(name = "economy_seat")
    private Integer economySeat;

    @Column(name = "flight_status")
    private String flightStatus;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "iata")
    private String iata;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // join departure
    // join arrival
    // join ariline

}
