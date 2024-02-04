package com.finalproject.Tiket.Pesawat.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "ticket_amount")
    private Integer ticketAmount;

    @Column(name = "fare_amount")
    private BigDecimal fareAmount;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "valid_until")
    private Date validUntil;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;


}
