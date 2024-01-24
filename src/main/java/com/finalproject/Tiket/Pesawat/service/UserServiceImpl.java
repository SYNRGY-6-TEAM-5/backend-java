package com.finalproject.Tiket.Pesawat.service;

import com.cloudinary.utils.StringUtils;
import com.finalproject.Tiket.Pesawat.dto.user.request.UpdateProfileRequest;
import com.finalproject.Tiket.Pesawat.dto.user.response.UpdateProfileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.dto.user.response.UserDetailsResponse;
import com.finalproject.Tiket.Pesawat.exception.EmailAlreadyRegisteredHandling;
import com.finalproject.Tiket.Pesawat.exception.ExceptionHandling;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.Images;
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
import org.springframework.web.multipart.MultipartFile;

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
    public UploadFileResponse uploadFile(String fileName, MultipartFile file) {
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
                if (fileName.isEmpty()) {
                    return null;
                }
                if (file.isEmpty()) {
                    return null;
                }
                String url = cloudinaryService.uploadFile(file, "user-images");
                Images image = Images.builder()
                        .name(fileName)
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
    public UploadFileResponse editFile(MultipartFile file) {
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

                if (user.getImages() == null || StringUtils.isEmpty(user.getImages().getUrl())) {
                    throw new ExceptionHandling("User does not have a profile image");
                }
                // Extract public_id from the Cloudinary URL
                String publicId = Utils.extractPublicId(user.getImages().getUrl());
                if (file.isEmpty()) {
                    throw new UnauthorizedHandling("Error Uploading File");
                }
                String url = cloudinaryService.editFile(file, publicId);
                if (url == null) {
                    throw new ExceptionHandling("Error editing file");
                }
//                Images image = Images.builder()
//                        .name(user.getImages().getName())
//                        .url(url)
//                        .build();
//
//                user.setImages(image);
//                userRepository.save(user);

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
            if (userOptional.isPresent()) {
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

    @Override
    public UpdateProfileResponse editProfile(UpdateProfileRequest updateProfileRequest) {
        // get signed
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
                user.setFullname(updateProfileRequest.getFullName());
                user.setBirthDate(updateProfileRequest.getDob());
                user.setPhoneNumber(updateProfileRequest.getPhoneNumber());
                user.setLastModified(Utils.getCurrentDateTimeAsDate());
                userRepository.save(user);

            } else if (principal instanceof String) {
                throw new UnauthorizedHandling("User not authenticated");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionHandling(e.getMessage());
        }
        return UpdateProfileResponse.builder()
                .success(true)
                .message("success update profile")
                .build();
    }

    @Override
    public UserDetailsResponse getUserDetails() {
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
                String imageUrl = (user.getImages() != null) ? user.getImages().getUrl() : null;

                return UserDetailsResponse.builder()
                        .success(true)
                        .id(user.getUuid().toString())
                        .imageUrl(imageUrl)
                        .fullName(user.getFullname())
                        .dob(user.getBirthDate())
                        .roleName(user.getRole().getRoleName().name())
                        .createdAt(user.getCreatedAt())
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
