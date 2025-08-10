package blog.jungmini.me.s3.service;

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

    public S3Service(AmazonS3 amazonS3Client, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    public String generatePresignedUploadUrl(String key, Duration expiration) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expirationDate);

        URL presignedUrl = amazonS3Client.generatePresignedUrl(request);
        return presignedUrl.toString();
    }

    public String getPublicUrl(String key) {
        return amazonS3Client.getUrl(bucket, key).toString();
    }
}
