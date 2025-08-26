package com.tamamhuda.minimart;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
public class KeyGenerationTest {

    @Test
    public void generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256); // for HS256
        SecretKey secretKey = keyGenerator.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        log.info("Generated Secret Key (Base64): {}", base64Key);
    }
}