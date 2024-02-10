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
public class RequestWebhookXenditPaid {
    private Date updated;
    private Date created;
    private String payment_id;
    private String callback_virtual_account_id;
    private String owner_id;
    private String external_id;
    private String account_number;
    private String bank_code;
    private String amount;
    private Date transaction_timestamp;
    private String id;
}
