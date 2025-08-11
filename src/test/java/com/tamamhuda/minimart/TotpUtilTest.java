package com.tamamhuda.minimart;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class TotpUtilTest {

    private static final String HMAC_ALGO = "HmacSHA1";

    public static SecretKey deriveUserSecretKey(String globalSecret, String username) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(globalSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        mac.init(keySpec);
        byte[] hmac = mac.doFinal(username.getBytes(StandardCharsets.UTF_8));

        byte[] secretKeyBytes = new byte[20];
        System.arraycopy(hmac, 0, secretKeyBytes, 0, secretKeyBytes.length);

        return new SecretKeySpec(secretKeyBytes, HMAC_ALGO);
    }

    private static final String GLOBAL_SECRET = "myGlobalSecretKey123";
    private static final String USERNAME = "user@example.com";

    @Test
    void testTotpGenerationAndVerification() throws Exception {
        SecretKey userSecret = deriveUserSecretKey(GLOBAL_SECRET, USERNAME);

        TOTPGenerator totpGenerator = new TOTPGenerator.Builder(userSecret.getEncoded())
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(6);
                    builder.withAlgorithm(HMACAlgorithm.valueOf(HMACAlgorithm.SHA256.toString())); // SHA256 and SHA512 are also supported
                })
                .withPeriod(Duration.ofSeconds(30))
                .build();


        String generatedOtp = totpGenerator.now();
        Assertions.assertThat(generatedOtp).hasSize(6);

        boolean valid = totpGenerator.verify(generatedOtp);
        Assertions.assertThat(valid).isTrue();

        boolean invalid = totpGenerator.verify("000000");
        Assertions.assertThat(invalid).isFalse();
    }
}
