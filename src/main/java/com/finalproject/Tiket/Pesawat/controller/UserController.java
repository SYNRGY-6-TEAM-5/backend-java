package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public UploadFileResponse uploadFileResponse(@RequestParam(name = "name") String fileName,
                                                 @RequestPart(name = "file") MultipartFile file) {
        return userService.uploadFile(fileName, file);
    }

    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse editFileResponse(@RequestPart(name = "file") MultipartFile file) {
        return userService.editFile(file);
    }

    @PutMapping("/profile")
    public UpdateProfileResponse updateProfileResponse(@RequestBody UpdateProfileRequest updateProfileRequest) {
        return userService.editProfile(updateProfileRequest);
    }


}
