package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/upload-file")
    public UploadFileResponse uploadFileResponse(UploadFileRequest uploadFileRequest) {
        return userService.uploadFile(uploadFileRequest);
    }


}
