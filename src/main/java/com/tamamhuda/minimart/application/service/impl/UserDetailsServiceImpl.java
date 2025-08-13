package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UnauthorizedException {
        User user =  userService.getByUsernameOrEmail(username);
        return userService.getByUsernameOrEmail(username);
    }

    private UserDetails validateUserIsVerified(User user) {
        if (user.isVerified()) {
            return user;
        } else {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied. User is not verified");
        }

    }
}
