package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.SuccesMessageDTO;
import com.finalproject.Tiket.Pesawat.dto.user.request.DeleteUserRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateUserFcmTokenRequest;
import com.finalproject.Tiket.Pesawat.dto.user.request.UploadImageRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserDetailsResponse;
import com.finalproject.Tiket.Pesawat.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UploadFileResponse uploadFile(UploadImageRequest uploadImageRequest);
    UploadFileResponse editFile(MultipartFile file);
    UpdateProfileResponse editProfile(UpdateProfileRequest updateProfileRequest);

    UserDetailsResponse getUserDetails();

    List<User> getAllUser(int page, int size);

    SuccesMessageDTO deleteUserById(DeleteUserRequest deleteUserRequest);

    SuccesMessageDTO setFcmToken(UpdateUserFcmTokenRequest userFcmTokenRequest);
}
