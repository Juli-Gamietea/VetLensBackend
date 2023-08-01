package com.api.vetlens.rest;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.springframework.web.multipart.MultipartFile;


public interface MachineLearningClient {
    @RequestLine("POST /")
    @Headers("Content-Type: multipart/form-data")
    Response uploadFile(@Param("image") MultipartFile file);
}
