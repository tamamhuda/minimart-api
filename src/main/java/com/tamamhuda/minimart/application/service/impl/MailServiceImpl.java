package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.MailService;
import com.tamamhuda.minimart.domain.entity.User;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailtrapClient client;

    @Value("${spring.mailtrap.sender-email}")
    private String mailtrapSenderMail;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.mailtrap.template.verification_email_id}")
    private String templateVerificationEmailId;

    @Value("${spring.mailtrap.template.verification_url}")
    private String verificationUrl;

    public void sendEmailVerification(User user, String token) {
        String verifyUrl = verificationUrl + "?token=" + token;
        String firstName = user.getFullName().split("\\s+")[0];

        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address(mailtrapSenderMail))
                .to(List.of(new Address(user.getEmail())))
                .templateUuid(templateVerificationEmailId)
                .templateVariables(Map.of(
                        "APP_NAME", appName,
                        "FIRST_NAME", firstName,
                        "VERIFY_URL", verifyUrl,
                        "EXPIRY", "5 minutes"
                ))
                .build();

        client.send(mail);
    }
}
