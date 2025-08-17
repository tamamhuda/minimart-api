package com.tamamhuda.minimart.common.util;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class TotpUtils {

    @Value("${spring.totp.global-secret}")
    private String globalSecret;

    @Value("${spring.totp.expiration-in-minutes}")
    private int expirationInMinutes;

    private static final String HMAC_ALGO = "HmacSHA1";

    public SecretKey deriveUserSecretKey(String username) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        SecretKey keySpec = new SecretKeySpec(globalSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        mac.init(keySpec);
        byte[] hmac = mac.doFinal(username.getBytes(StandardCharsets.UTF_8));

        byte[] secretKeyBytes = new byte[20];
        System.arraycopy(hmac, 0, secretKeyBytes, 0, secretKeyBytes.length);

        return new SecretKeySpec(secretKeyBytes, HMAC_ALGO);
    }


    public TOTPGenerator totpGenerator(SecretKey secretKey) {
        return new TOTPGenerator.Builder(secretKey.getEncoded())
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(6);
                    builder.withAlgorithm(HMACAlgorithm.valueOf(HMACAlgorithm.SHA256.toString())); // SHA256 and SHA512 are also supported
                })
                .withPeriod(Duration.ofMinutes(expirationInMinutes))
                .build();
    }


    public String generateCurrentOtp(String username)  {
        try {
            SecretKey secretKey = deriveUserSecretKey(username);
            TOTPGenerator totpGenerator = totpGenerator(secretKey);
            return totpGenerator.now();
        } catch (Exception e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed trying to generate OTP");
        }
    }

    public boolean verifyOtp(String username, String otp)  {
        try {
            SecretKey secretKey = deriveUserSecretKey(username);
            TOTPGenerator totpGenerator = totpGenerator(secretKey);
            return totpGenerator.verify(otp);
        } catch (Exception e) {
            return false;
        }
    }


}
