package com.tamamhuda.minimart.config;

import com.xendit.XenditClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XenditConfig {
    @Value("${spring.xendit.secret-key}")
    private String xendit_secretKey;

    @Bean
    public XenditClient xenditClient() {

        return new XenditClient.Builder()
                .setApikey(xendit_secretKey)
                .build();
    }
}
