package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;

public interface UserService {

    UploadFileResponse uploadFile(UploadFileRequest uploadFileRequest);
}
