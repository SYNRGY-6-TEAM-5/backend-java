package com.finalproject.Tiket.Pesawat.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestBooking {

    private String tripe_type;
    private Integer total_passenger;
    private Date expired_time;
    private Integer total_amount;
    private Boolean full_protection;
    private Boolean bag_insurance;
    private Boolean flight_delay;
    private String payment_method;
    private String status;

}
