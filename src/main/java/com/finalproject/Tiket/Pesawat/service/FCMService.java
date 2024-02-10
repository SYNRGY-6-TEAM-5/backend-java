package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.firebase.request.NotificationRequest;

import java.util.concurrent.ExecutionException;

public interface FCMService {

    void sendMessageToToken(NotificationRequest request) throws InterruptedException, ExecutionException;
}
