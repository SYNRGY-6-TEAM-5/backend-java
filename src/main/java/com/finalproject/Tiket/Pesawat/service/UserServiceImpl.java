package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.Images;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UploadFileResponse uploadFile(UploadFileRequest uploadFileRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                Optional<User> userOptional = userRepository
                        .findByEmailAddress(((UserDetailsImpl) principal).getUsername());
                if (userOptional.isEmpty()) {
                    throw new UnauthorizedHandling("User Not Found");
                }
                User user = userOptional.get();
                if (uploadFileRequest.getName().isEmpty()) {
                    return null;
                }
                if (uploadFileRequest.getFile().isEmpty()) {
                    return null;
                }
                String url = cloudinaryService.uploadFile(uploadFileRequest.getFile(), "user-images");
                Images image = Images.builder()
                        .name(uploadFileRequest.getName())
                        .url(url)
                        .build();

                if (image.getUrl() == null) {
                    return null;
                }
                user.setImages(image);
                userRepository.save(user);
                return UploadFileResponse.builder()
                        .success(true)
                        .urlImage(url)
                        .message("Success upload image")
                        .build();
            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionHandling(e.getMessage());
        }
        throw new UnauthorizedHandling("Unknown principal type");
    }
}
