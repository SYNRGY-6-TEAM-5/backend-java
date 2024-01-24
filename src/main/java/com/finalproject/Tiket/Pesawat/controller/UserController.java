package com.finalproject.Tiket.Pesawat.controller;


import com.finalproject.Tiket.Pesawat.dto.user.response.UserDetailsResponse;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
//@PreAuthorize("hasRole('USER')")
public class UserController {
    /*
    Controller untuk User -> {
       Mengelola operasi yang berkaitan dengan role user,
       Seperti CRUD (Create, Read, Update, Delete)
    }
     */

    @Autowired
    private UserService userService;


    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFileResponse> uploadFileResponse(@RequestParam(name = "name") String fileName,
                                                                 @RequestPart(name = "file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadFile(fileName, file));
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



}
