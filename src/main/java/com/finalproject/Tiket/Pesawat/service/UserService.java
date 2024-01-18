package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UploadFileResponse uploadFile(String fileName, MultipartFile file);
    UploadFileResponse editFile(MultipartFile file);

    Boolean saveNewUserFromOauth2(User user, String imageUrl);

    UpdateProfileResponse editProfile(UpdateProfileRequest updateProfileRequest);

}
