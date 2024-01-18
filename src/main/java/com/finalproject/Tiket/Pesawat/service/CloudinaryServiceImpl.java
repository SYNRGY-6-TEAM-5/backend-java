package com.finalproject.Tiket.Pesawat.service;

import com.cloudinary.Cloudinary;
import com.finalproject.Tiket.Pesawat.dto.user.response.UploadFileResponse;
import com.finalproject.Tiket.Pesawat.exception.UnauthorizedHandling;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.security.service.UserDetailsImpl;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String editFile(MultipartFile file, String publicId) {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("public_id", publicId);
            options.put("overwrite", "true");
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            return cloudinary.url().secure(true).generate(publicId);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
