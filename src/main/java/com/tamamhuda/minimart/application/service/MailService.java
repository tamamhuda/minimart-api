package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.domain.entity.User;

public interface MailService {

    void sendEmailVerification(User user, String token);
}
