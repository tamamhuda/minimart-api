package com.tamamhuda.minimart;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerationTest {

    @Test
    public void generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256); // for HS256
        SecretKey secretKey = keyGenerator.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated JWT Secret Key (Base64):");
        System.out.println(base64Key);
    }
}