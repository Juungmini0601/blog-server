package org.logly.s3.service;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@Service
public class S3Service {
    private final AmazonS3 amazonS3Client;
    private final String bucket;
    private final String cdnDomain;
    private final String originPath;

    public S3Service(
            AmazonS3 amazonS3Client,
            @Value("${cloud.aws.s3.bucket}") String bucket,
            @Value("${cdn.domain}") String cdnDomain,
            @Value("${cdn.originPath}") String originPath) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
        this.cdnDomain = cdnDomain;
        this.originPath = originPath;
    }

    public String generatePresignedUploadUrl(String key, Duration expiration) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expirationDate);

        URL presignedUrl = amazonS3Client.generatePresignedUrl(request);
        return presignedUrl.toString();
    }

    public String getCdnUrl(String key) {
        String viewerPath = key;
        if ("/images".equals(originPath)) {
            String prefix = "images/";
            if (viewerPath.startsWith(prefix)) {
                viewerPath = viewerPath.substring(prefix.length());
            }
        }

        return String.format("https://%s/%s", cdnDomain, viewerPath);
    }

    public String getPublicUrl(String key) {
        return amazonS3Client.getUrl(bucket, key).toString();
    }
}
