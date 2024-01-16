package com.finalproject.Tiket.Pesawat.controller;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFileResponse(@RequestParam(name = "name") String fileName,
                                                 @RequestPart(name = "file") MultipartFile file) {
        return userService.uploadFile(fileName, file);
    }


}
