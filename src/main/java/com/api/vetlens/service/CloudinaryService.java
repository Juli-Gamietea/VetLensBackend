package com.api.vetlens.service;

import com.api.vetlens.exceptions.ApiException;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile, String username, String dogName) {
        try{
            String fileId = username + "|" + LocalDate.now() + "|" + dogName + "(" + UUID.randomUUID() + ")";
            return cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", fileId))
                    .get("url")
                    .toString();
        }catch (IOException e){
            throw new ApiException("Ocurrio un problema subiendo el archivo a Cloudinary");
        }

    }
}
