package com.finalproject.Tiket.Pesawat.repository;

import com.finalproject.Tiket.Pesawat.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagesRepository extends JpaRepository<Images, UUID> {
}
