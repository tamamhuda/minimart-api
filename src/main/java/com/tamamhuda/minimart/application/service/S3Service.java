package com.tamamhuda.minimart.application.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    public String uploadImage(MultipartFile file, String prefix);

    public void proxyImage(HttpServletResponse response, String prefix, String imageUrl);

}
