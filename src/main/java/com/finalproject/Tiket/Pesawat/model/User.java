package com.finalproject.Tiket.Pesawat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
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

    @JsonIgnore
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

    @Column(name = "fcm_token")
    private String fcmToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Images images;

    @Column(name = "created_at")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "last_modified")
    @Timestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> booking;

}
