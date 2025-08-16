package org.logly.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
    private String accessKey;

    private String secretKey;

    private String region;

    public S3Config(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        this.accessKey = accessKey;
        this.region = region;
        this.secretKey = secretKey;
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
