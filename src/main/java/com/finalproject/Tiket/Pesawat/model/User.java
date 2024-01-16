package com.finalproject.Tiket.Pesawat.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID uuid;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "bitrh_date")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(name = "phone_num")
    private Long phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Images images;

    @Column
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

}
