package com.api.vetlens.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    private static final String CLOUD_NAME = "db3ti85we";
    private static final String API_KEY = "833719695359276";
    private static final String API_SECRET = "QLg3H0hAbCWQ9YOzY6n5YiIVsLw";

    @Bean
    public Cloudinary cloudinary() {
       return new Cloudinary(
               ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true)
       );
    }
}
