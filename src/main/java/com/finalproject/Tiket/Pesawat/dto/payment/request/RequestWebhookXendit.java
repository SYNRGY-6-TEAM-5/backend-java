package com.finalproject.Tiket.Pesawat.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestWebhookXendit {
    private String id;
    private String owner_id;
    private String external_id;
    private String merchant_code;
    private String account_number;
    private String bank_code;
    private String name;
    private Boolean is_closed;
    private Date expiration_date;
    private Boolean is_single_use;
    private String status;
    private Date created;
    private Date updated;
}
