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
@Table(name = "arrival")
public class Arrival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arrival_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_id")
    private Airport airport;

    @Column
    private String terminal;

    @Column(name = "scheduled_time")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;

    @Column(name = "created_at")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
