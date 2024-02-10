package com.finalproject.Tiket.Pesawat.controller;


import com.finalproject.Tiket.Pesawat.dto.SuccesMessageDTO;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateUserFcmTokenRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UploadImageRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserDetailsResponse;
import com.finalproject.Tiket.Pesawat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
//@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFileResponse> uploadFileResponse(@Validated @ModelAttribute UploadImageRequest uploadImageRequest) {
        return ResponseEntity.ok(userService.uploadFile(uploadImageRequest));
    }

    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFileResponse> editFileResponse(@RequestPart(name = "file") MultipartFile file) {
        return ResponseEntity.ok(userService.editFile(file));
    }

    @PutMapping("/profile")
    public ResponseEntity<UpdateProfileResponse> updateProfileResponse(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(userService.editProfile(updateProfileRequest));
    }

    @GetMapping("/detail-user")
    public ResponseEntity<UserDetailsResponse> getUserDetails() {
        UserDetailsResponse userDetailsResponse = userService.getUserDetails();
        return ResponseEntity.ok(userDetailsResponse);
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<SuccesMessageDTO> setFcmToken(@RequestBody
                                                        @Valid
                                                        UpdateUserFcmTokenRequest userFcmTokenRequest) {
        SuccesMessageDTO response = userService.setFcmToken(userFcmTokenRequest);
        return ResponseEntity.ok(response);
    }


}
