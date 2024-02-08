package com.finalproject.Tiket.Pesawat.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;


@RestController
@RequestMapping("/api/v1/server")
@Log4j2
public class ServerController {

    @Autowired
    private Environment env;

    @GetMapping("/check-protocol")
    public String checkProtocol() {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        return "Current protocol is " + protocol;
    }
}
