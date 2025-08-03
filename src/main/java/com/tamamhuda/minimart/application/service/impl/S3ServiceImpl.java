package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.S3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl  implements S3Service {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp");

    private final S3Client s3Client;

    protected final String staticFiles = "upload/images";

    @Value("${spring.cloud.config.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile file, String prefix)  {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank() || !isValidateImageExtension(originalFilename)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file extension");
        }


        String imageUrl = UUID.randomUUID() + "-" + slugifyFilename(originalFilename) + "." + getFileExtensions(originalFilename);
        String key = getKey(prefix, imageUrl);

        try {

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

        } catch (IOException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }

        return imageUrl;
    }

    private String slugifyFilename(String filename) {
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));

        // Remove non-ASCII characters, replace spaces/underscores with hyphens, strip symbols
        return nameWithoutExtension
                .replaceAll("[^\\w\\s-]", "")           // Remove special characters
                .replaceAll("[\\s_]+", "-")             // Replace spaces and underscores with hyphens
                .replaceAll("-{2,}", "-")               // Collapse multiple hyphens
                .toLowerCase()
                .trim();
    }

    private String getKey(String prefix, String imageUrl) {

        return staticFiles + "/" + prefix + "/" + imageUrl;
    }
    private boolean isValidateImageExtension(String filename) {
        String fileExtension = getFileExtensions(filename);
        return ALLOWED_EXTENSIONS.contains(fileExtension);
    }

    private String getFileExtensions(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ?  filename.substring(dotIndex + 1) : filename;
    }


    @Override
    public void proxyImage(HttpServletResponse response, String prefix, String imageUrl) {
        String key = getKey(prefix, imageUrl);
        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder().bucket(bucket).key(key).build())) {
            response.setContentType(s3Object.response().contentType());
            response.setHeader("Content-Disposition", "inline");
            response.flushBuffer();
            StreamUtils.copy(s3Object, response.getOutputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to load image ");
        }
    }
}
