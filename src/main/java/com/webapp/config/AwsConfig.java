package com.webapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    private final AmazonS3 s3client;

    public AwsConfig(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAIEWPZCQZKOH62F6Q",
                "aiYsfAKNKOyP1gkWy7V0gg4fXuvQLkxlMKTSc5Md"
        );
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }

    @Bean
    public AmazonS3 getS3client() {
        return s3client;
    }
}
