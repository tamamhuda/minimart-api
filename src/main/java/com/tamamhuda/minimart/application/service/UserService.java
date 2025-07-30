package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    public User getUserByUsername(String username) throws UnauthorizedException;

    public User getUserByEmail(String email) throws UnauthorizedException;

    public UserDetails getByUsernameOrEmail(String username) throws UnauthorizedException;

    public User createUser(User user);

    public UserDetails validateCredentials(String username, String password) throws UnauthorizedException;
}
