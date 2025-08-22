package com.tamamhuda.minimart.config;


import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
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

        String fileName = ".env." + activeProfile;

       if(!activeProfile.equals("test")) {
           try {
               loadDotEnv(fileName);
           } catch (DotenvException e) {
               log.error(e.getMessage());
           }

       } else {
           try {
               loadDotEnv(fileName);
           } catch (DotenvException ignored){};
       }

    }

    private void loadDotEnv(String filename) {
        Dotenv dotenv = Dotenv.configure()
                .filename(filename)
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(e -> {
            System.setProperty(e.getKey(), e.getValue());
        });
    }

}