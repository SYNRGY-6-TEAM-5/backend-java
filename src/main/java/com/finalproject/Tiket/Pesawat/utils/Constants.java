package com.finalproject.Tiket.Pesawat.utils;

public final class Constants {
    public static final String CONSTANT_EMAIL_TEST_SIGNUP = "testAeroSwift@gmail.com";
    public static final String CONSTANT_EMAIL_TEST_FORGOT = "testAeroSwift2@gmail.com";

    public static final long PAYMENT_EXPIRED_TIME = 180 * 60 * 1000;

    public static final long OTP_EXPIRATION_DURATION = 5 * 60 * 1000; // 5 minutes

    public static final String XENDIT_API_BASE_URL = "https://api.xendit.co/callback_virtual_accounts/";


    public static final String CONSTANT_PAYMENT_STATUS_PENDING = "PENDING";
    public static final String CONSTANT_PAYMENT_STATUS_SUCCESS = "SUCCESS";
    public static final String CONSTANT_PAYMENT_STATUS_FAILED = "FAILED";

    public static final String CONSTANT_PAYMENT_SUCCESS_URL = "http://localhost:8080/api/v1/payment/success-payment";
    public static final String CONSTANT_PAYMENT_FAILED_URL = "http://localhost:8080/api/v1/payment/failed-payment";

    public static final String STRIPE_HEADER_REQEUST = "Stripe-Signature";
    public static final String STRIPE_SESSION_COPMLETED = "checkout.session.completed";

    public static final String CONSTANT_CURRENCY = "IDR";
}
