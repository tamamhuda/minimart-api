package com.tamamhuda.minimart.config;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Value("${spring.mailtrap.token}")
    private String mailtrap_token;

    @Bean
    public MailtrapClient mailtrapClient() {
        MailtrapConfig config = new MailtrapConfig.Builder()
                .token(mailtrap_token)
                .build();

        return MailtrapClientFactory.createMailtrapClient(config);

    }
}
