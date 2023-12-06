package com.api.vetlens.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final String bucketName = System.getenv("AWS_BUCKET_NAME");
    private final AmazonS3 s3Client;

    public String upload(String id, MultipartFile multipartFile ) {
        File fileObj = convertMultiPartFileToFile(multipartFile);
        s3Client.putObject(new PutObjectRequest(bucketName, id, fileObj).withCannedAcl(CannedAccessControlList.PublicRead));
        fileObj.delete();
        return s3Client.getUrl(bucketName, id).toString();
    }
    public void removeDogPhoto(String dogName) {
        s3Client.deleteObject(bucketName, "profile-" + dogName);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

     /*public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
