package com.tamamhuda.minimart.application.service;


import com.tamamhuda.minimart.domain.enums.OtpStatus;

public interface VerificationService {

    String generateOtpForUser(String username);

    OtpStatus verifyOtpForUser(String token);
}