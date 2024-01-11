package com.finalproject.Tiket.Pesawat.utils;


import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// this is initializer for data role
@Component
@Log4j2
public class RoleInitizalizer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception { // handling error cannot init data role
        initializeRoles();
    }
    private void initializeRoles() {
        try {
            if (roleRepository.count() == 0) {
                log.info("intialize data role");
                // Jika tidak ada, tambahkan role yang dibutuhkan
                Role userRole = new Role();
                userRole.setRoleName(EnumRole.USER);
                Role adminRole = new Role();
                adminRole.setRoleName(EnumRole.ADMIN);
                roleRepository.saveAll(Arrays.asList(userRole, adminRole));
                log.info("success add data");
            }

        } catch (Exception e){
            log.error("Error initialize data", e.getCause() + " " + e.getMessage());
        }
    }
}
