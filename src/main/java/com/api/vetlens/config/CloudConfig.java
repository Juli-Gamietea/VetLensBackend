package com.api.vetlens.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

@Configuration
public class CloudConfig {
    @Bean
    public SsmClient ssmClient() {
        return SsmClient.builder()
                .region(Region.SA_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public AmazonS3 s3Client() {
        SsmClient ssmClient = ssmClient();
        String secretKey = ssmClient.getParameter(GetParameterRequest
                .builder()
                .name("secret_key")
                .build()).parameter().value();
        String accessSecret = ssmClient.getParameter(GetParameterRequest
                .builder()
                .name("access_secret")
                .build()).parameter().value();
        //String accessKey = System.getenv("AWS_SECRET_KEY");
        //String accessSecret = System.getenv("AWS_ACCESS_SECRET");
        String region = Region.SA_EAST_1.toString();

        AWSCredentials credentials = new BasicAWSCredentials(secretKey, accessSecret);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region).build();
    }


}
