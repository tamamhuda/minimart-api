package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UnauthorizedException {
        // returns your User entity
        return userService.getByUsernameOrEmail(username);
    }
}
