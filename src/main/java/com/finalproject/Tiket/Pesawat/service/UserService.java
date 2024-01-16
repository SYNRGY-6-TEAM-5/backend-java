package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.model.User;

public interface UserService {

    UploadFileResponse uploadFile(UploadFileRequest uploadFileRequest);

    Boolean saveNewUserFromOauth2(User user, String imageUrl);
}
