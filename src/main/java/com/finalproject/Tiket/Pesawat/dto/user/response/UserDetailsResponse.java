package com.finalproject.Tiket.Pesawat.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsResponse {
    private Boolean success;
    private String id;
    private String imageUrl;
    private String fullName;
    private Date dob;
    private String roleName;
    private Long phoneNum;
    private Date createdAt;
}
