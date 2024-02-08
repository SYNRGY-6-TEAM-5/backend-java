package com.finalproject.Tiket.Pesawat.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class ProtocolChecker {

    @Autowired
    private ServerProperties serverProperties;

    public String getCurrentProtocol() {
        return serverProperties.getSsl() != null ? "HTTPS" : "HTTP";
    }
}
