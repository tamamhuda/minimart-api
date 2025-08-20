package com.tamamhuda.minimart.config;


import io.github.cdimascio.dotenv.Dotenv;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext context) {
        String activeProfile = context.getEnvironment().getActiveProfiles()[0];
        log.info("Active profile: {}", activeProfile);

        Dotenv dotenv = Dotenv.configure()
                .filename(".env." + activeProfile)
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(e -> {
            System.setProperty(e.getKey(), e.getValue());
        });
    }
}