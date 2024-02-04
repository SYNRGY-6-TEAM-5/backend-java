package com.finalproject.Tiket.Pesawat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "travel_docs")
public class TravelDocs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_doc_id")
    private Integer travelDocId;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "doc_number")
    private String docNumber;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "file")
    private String fileName;

    @Column(name = "valid")
    private Boolean valid;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "passanger_id")
    private Passenger passenger;
}
