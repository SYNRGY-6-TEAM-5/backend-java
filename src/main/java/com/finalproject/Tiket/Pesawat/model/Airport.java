package com.finalproject.Tiket.Pesawat.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
@Table(name = "airport")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Integer id;

    @Column(name = "airport_name")
    private String airPortName;

    @Column(name = "iata_code")
    private String iataCode;

    @Column(name = "gmt")
    private String gmt;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_iata_code")
    private String cityIataCode;

    @Column(name = "country_iso_code")
    private String countryIsoCode;

    @Column(name = "country_name")
    private String countryName;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Arrival> arrivals;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Departure> departures;

}
