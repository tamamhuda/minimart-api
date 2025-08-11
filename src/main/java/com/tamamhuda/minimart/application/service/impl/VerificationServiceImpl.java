package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.OtpTokenDto;
import com.tamamhuda.minimart.application.service.VerificationService;
import com.tamamhuda.minimart.common.util.OtpTokenUtils;
import com.tamamhuda.minimart.common.util.TotpUtils;
import com.tamamhuda.minimart.domain.enums.OtpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final TotpUtils totpUtils;
    private final UserServiceImpl userServiceImpl;
    private final OtpTokenUtils otpTokenUtils;

    @Override
    public String generateOtpForUser(String username){
        String otp = totpUtils.generateCurrentOtp(username);
        return otpTokenUtils.generateOtpToken(username, otp);
    }

    @Override
    public OtpStatus verifyOtpForUser(String token) {
        try {
            OtpTokenDto tokenClaims = otpTokenUtils.extractOtpToken(token);
            String username = tokenClaims.getUsername();
            String otp = tokenClaims.getOtp();

            boolean verified = totpUtils.verifyOtp(username, otp);

            if (verified) {
                userServiceImpl.verifyUser(username);
                return OtpStatus.SUCCESS;
            } else {
                return OtpStatus.FAILED;
            }

        } catch (Exception e) {
            return otpTokenUtils.extractValidateOtpToken(token);
        }

    }
}
