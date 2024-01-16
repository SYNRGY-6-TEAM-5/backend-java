package com.finalproject.Tiket.Pesawat.service;

import com.finalproject.Tiket.Pesawat.dto.user.request.UploadFileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.exception.EmailAlreadyRegisteredHandling;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Images;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.RoleRepository;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import com.finalproject.Tiket.Pesawat.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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

                log.info("user a " + ((UserDetailsImpl) principal).getUsername());
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

    @Override
    public Boolean saveNewUserFromOauth2(User user, String imageUrl) {
        try {
            Optional<User> userOptional = userRepository.findByEmailAddress(user.getEmailAddress());
            if (userOptional.isPresent()){
                throw new EmailAlreadyRegisteredHandling();
            }
//            Optional<Role> optionalUserRole = roleRepository.findByRoleName(EnumRole.USER);
            Images image = Images.builder()
                    .name(user.getFullname())
                    .url(imageUrl)
                    .build();

//            user.setRole(); todo
            user.setImages(image);
            user.setFullname(user.getFullname());
            user.setCreatedAt(Utils.getCurrentDateTimeAsDate());
            userRepository.save(user);
            log.info("sukses create user");
        } catch (Exception e) {
            log.error("Failed Save new Oauth2 User " + e.getMessage());
            return false;
        }
        return true;
    }
}
