package com.finalproject.Tiket.Pesawat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private UUID uuid;

    @Column(name = "name_image")
    private String name;

    @Column(name = "url_image")
    private String url;
}
