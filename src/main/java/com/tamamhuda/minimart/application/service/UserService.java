package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

public interface UserService {

    public User getUserByUsername(String username) throws ResponseStatusException;

    public User getUserByEmail(String email) throws UnauthorizedException;

    public UserDetails getByUsernameOrEmail(String username) throws ResponseStatusException;

    public User createUser(User user);

    public UserDetails validateCredentials(String username, String password) throws UnauthorizedException;
}
