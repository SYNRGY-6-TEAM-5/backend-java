package com.finalproject.Tiket.Pesawat.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/server")
@Log4j2
public class ServerController {
    @Autowired
    private ServerProperties serverProperties;

    @GetMapping("/check-protocol")
    public String getProtocol() {
        log.info("executing get protocol");
        return serverProperties.getSsl() != null ? "HTTPS" : "HTTP";
    }
}
