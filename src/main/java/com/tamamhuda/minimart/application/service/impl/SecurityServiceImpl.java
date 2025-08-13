package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    public String username() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name.equals("anonymousUser")) {
            return null;
        }
        return name;
    }
}
