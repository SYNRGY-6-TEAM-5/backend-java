package com.finalproject.Tiket.Pesawat;

import com.finalproject.Tiket.Pesawat.configuration.AeroConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AeroConfigProperties.class)
public class TiketPesawatApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiketPesawatApplication.class, args);
	}

}
