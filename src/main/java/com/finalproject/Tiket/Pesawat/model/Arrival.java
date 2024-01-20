package com.finalproject.Tiket.Pesawat.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "airport_id")
    @Fetch(FetchMode.SELECT)
    private Airport airport;


}
